package com.nextflix.app.seriesList.data.repository

import android.util.Log
import coil.network.HttpException
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.core.network.SupabaseClient.client
import com.nextflix.app.seriesList.data.local.series.SeriesDatabase
import com.nextflix.app.seriesList.data.mappers.toSeries
import com.nextflix.app.seriesList.data.mappers.toSeriesEntity
import com.nextflix.app.seriesList.domain.model.Series
import com.nextflix.app.seriesList.domain.repository.SeriesListRepository
import com.nextflix.app.seriesList.utils.Resource
import com.nextflix.app.watchLater.data.model.WatchLaterSeries
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class SeriesListRepositoryImpl @Inject constructor(
    private val seriesApi: SeriesApi,
    private val seriesDatabase: SeriesDatabase
) : SeriesListRepository {

    override suspend fun getSeriesList(
        forceFetchFromRemote: Boolean,
        page: Int
    ): Flow<Resource<List<Series>>> {
        return flow {
            emit(Resource.Loading(true))

            val localSeriesList = seriesDatabase.seriesDao.getLocalSeries(client.auth.currentUserOrNull()?.id.toString())

            Log.e("series123",localSeriesList.toString())
            //if room is empty only then get from api
            val shouldLoadLocalSeries = localSeriesList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalSeries) {
                emit(Resource.Success(data = localSeriesList.map { seriesEntity ->
                    seriesEntity.toSeries()
                }))

                emit(Resource.Loading(false))
                return@flow
            }

            //gte data from api
            val seriesListFromApi = try {
                seriesApi.getSeriesList(page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("1234listexcep", e.toString())
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }

            //get watchlater series from db
            val watchLaterSeriesIds = try {
                val response = client.from("WatchLater")
                    .select(Columns.list("series_id"))
                    .decodeList<Map<String, Int>>() // Decode as a list of maps

                // Extract the "series_id" values into a list of integers
                response.map { it["series_id"] ?: -1 }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }

            val seriesEntities = seriesListFromApi.results.map { seriesDto ->
                val seriesEntity = seriesDto.toSeriesEntity(client.auth.currentUserOrNull()?.id.toString())
                seriesEntity.watch_later =
                    watchLaterSeriesIds.contains(seriesEntity.id) // Set watchLater flag
                seriesEntity
            }.sortedByDescending { it.popularity } // Sort by popularity in descending order

            seriesDatabase.seriesDao.upsertSeriesList(seriesEntities)

            emit(
                Resource.Success(
                    seriesEntities.map {
                        it.toSeries()
                    }
                )
            )
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getUserSeriesList(): Flow<Resource<List<Series>>> {
        return flow {
            emit(Resource.Loading(true))

            //get all ids of series in watch later
            val userSList = try {
                val userId = client.auth.currentUserOrNull()?.id
                client.from("WatchLater")
                    .select(Columns.list("series_id", "series_name", "poster_path")) {
//                        filter {
//                            if (userId != null) {
//                                eq("user_id", userId)
//                            }
//                        }
                    }
                    .decodeList<WatchLaterSeries>()
            } catch (e: Exception) {
                Log.e("error1234", e.toString())
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }
            val seriesList = userSList.map { it.toSeries() }

            emit(Resource.Success(seriesList))

            emit(Resource.Loading(false))
        }
    }

    override suspend fun getSearchSeriesList(query: String): Flow<Resource<List<Series>>> {
        return flow {
            emit(Resource.Loading(true))

            val searchListFromApi = try {
                seriesApi.getSearchSeriesList(query = query)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }

            Log.e("ser125", searchListFromApi.toString())

            val watchLaterSeriesIds = try {
                val response = client.from("WatchLater")
                    .select(Columns.list("series_id"))
                    .decodeList<Map<String, Int>>() // Decode as a list of maps

                // Extract the "series_id" values into a list of integers
                response.map { it["series_id"] ?: -1 }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }

            val seriesList = searchListFromApi.results.map { seriesDto ->
                val series = seriesDto.toSeries()
                series.copy(watch_later = series.id in watchLaterSeriesIds) // Set watch_later based on ID presence
            }.sortedByDescending { it.vote_count }

            emit(Resource.Success(seriesList))
            emit(Resource.Loading(false))


        }
    }

    override suspend fun addToWatchLater(
        seriesId: Int,
        seriesName: String,
        posterPath: String
    ): Result<Unit> {
        return try {
            client.from("WatchLater").upsert(
                WatchLaterSeries(
                    //userId = "123",
                    seriesId = seriesId,
                    seriesName = seriesName,
                    posterPath = posterPath
                )
            )

            //change value in room
            seriesDatabase.seriesDao.addToWatchLater(seriesId, client.auth.currentUserOrNull()?.id.toString())

            Log.d("SaveWatchLater123", "Row added to WatchLater successfully!")
            Result.success(Unit)  // Explicitly return success

        } catch (e: Exception) {
            Log.e("SaveWatchLater123", "Error during insertion: ${e.message}")
            Result.failure(e)  // Explicitly return failure
        }
    }

    override suspend fun removeFromWatchLater(seriesId: Int): Result<Unit> {
        return try {
            client.from("WatchLater").delete {
                filter {
                    eq("series_id", seriesId)
                    //Series::id eq seriesId
                }
            }

            //change value in room
            seriesDatabase.seriesDao.removeFromWatchLater(seriesId, client.auth.currentUserOrNull()?.id.toString())

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SaveWatchLater123", "Error during insertion: ${e.message}")
            Result.failure(e)  // Explicitly return failure
        }
    }

}
