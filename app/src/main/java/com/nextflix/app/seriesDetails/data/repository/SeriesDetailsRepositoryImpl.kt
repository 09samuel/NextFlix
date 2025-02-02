package com.nextflix.app.seriesDetails.data.repository

import android.util.Log
import coil.network.HttpException
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.core.network.SupabaseClient.client
import com.nextflix.app.seriesDetails.data.mappers.toEpisode
import com.nextflix.app.seriesDetails.data.mappers.toGeneralDetails
import com.nextflix.app.seriesDetails.data.mappers.toRecommendations
import com.nextflix.app.seriesDetails.data.model.SeriesProgress
import com.nextflix.app.seriesDetails.data.respond.episodes.EpisodesListDto
import com.nextflix.app.seriesDetails.data.respond.generalDetails.GeneralDetailsDto
import com.nextflix.app.seriesDetails.data.respond.recommendations.RecommendationsListDto
import com.nextflix.app.seriesDetails.data.respond.watchProvider.SeriesWatchProvidersDto
import com.nextflix.app.seriesDetails.domain.model.Episode
import com.nextflix.app.seriesDetails.domain.model.GeneralDetails
import com.nextflix.app.seriesDetails.domain.model.Recommendations
import com.nextflix.app.seriesDetails.domain.model.SeriesWatchProviders
import com.nextflix.app.seriesDetails.domain.repository.SeriesDetailsRepository
import com.nextflix.app.seriesList.data.local.series.SeriesDatabase
import com.nextflix.app.seriesList.utils.Resource
import com.nextflix.app.watchLater.data.model.WatchLaterSeries
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject

class SeriesDetailsRepositoryImpl @Inject constructor(
    private val seriesApi: SeriesApi,
    private val seriesDatabase: SeriesDatabase
) : SeriesDetailsRepository {

    override suspend fun getSeries(seriesId: Int): Flow<Resource<GeneralDetails>> {
        return flow {
            emit(Resource.Loading(true))

            val details: GeneralDetailsDto = try {
                seriesApi.getGeneralDetails(seriesId = seriesId)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows due to network issues"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows due to server issues"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "An unexpected error occurred"))
                return@flow
            }

            val generalDetails = details.toGeneralDetails()

            emit(Resource.Success(data = generalDetails))

            emit(Resource.Loading(false))

        }
    }

    override suspend fun getSeriesWatchProviders(
        id: Int,
        countryCode: String
    ): Flow<Resource<List<SeriesWatchProviders>>> {
        return flow {
            emit(Resource.Loading(true))

            val watchProviderList: SeriesWatchProvidersDto = try {
                seriesApi.getWatchProviders(seriesId = id)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows due to network issues"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows due to server issues"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "An unexpected error occurred"))
                return@flow
            }

            // Check if the country code exists in the response
            val countryWatchProvider = watchProviderList.results[countryCode]
            if (countryWatchProvider == null) {
                emit(Resource.Error(message = "No watch providers found for the specified country: $countryCode"))
                emit(Resource.Loading(false))
                return@flow
            }

            // Transforming API response to SeriesWatchProviders for the given country
            val seriesWatchProviders = mutableListOf<SeriesWatchProviders>()

            countryWatchProvider.flatrate?.mapTo(seriesWatchProviders) {
                SeriesWatchProviders(
                    display_priority = it.display_priority,
                    logo_path = it.logo_path ?: "",
                    provider_name = it.provider_name,
                    link = countryWatchProvider.link ?: "",
                    provider_type = "Flatrate"
                )
            }

            countryWatchProvider.buy?.mapTo(seriesWatchProviders) {
                SeriesWatchProviders(
                    display_priority = it.display_priority,
                    logo_path = it.logo_path ?: "",
                    provider_name = it.provider_name,
                    link = countryWatchProvider.link ?: "",
                    provider_type = "Buy"
                )
            }

            emit(Resource.Success(seriesWatchProviders))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getRecommendations(seriesId: Int): Flow<Resource<List<Recommendations>>> {
        return flow {
            emit(Resource.Loading(true))

            // Fetch recommendations from API
            val recommendations: RecommendationsListDto = try {
                seriesApi.getRecommendations(seriesId = seriesId)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows due to network issues"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows due to server issues"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "An unexpected error occurred"))
                return@flow
            }

            // Fetch watch later series IDs from database
            val watchLaterSeriesIds = try {
                val response = client.from("WatchLater")
                    .select(Columns.list("series_id"))
                    .decodeList<Map<String, Int>>() // Decode as a list of maps

                // Extract the "series_id" values into a list of integers
                response.map {
                    it["series_id"] ?: -1
                } // Replace -1 with a meaningful default if needed
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading watch later shows"))
                return@flow
            }

            // Map recommendations to include watch_later flag
            val recommendationsList = recommendations.results?.map { dto ->
                val recommendation = dto.toRecommendations()
                recommendation.copy(
                    watch_later = watchLaterSeriesIds.contains(recommendation.id)
                )
            } ?: emptyList()

            emit(Resource.Success(recommendationsList))
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
//                mapOf(
//                    "seriesId" to seriesId,
//                    "seriesName" to seriesName,
//                    "posterPath" to posterPath
//                )
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

    override suspend fun getEpisodes(
        seriesId: Int,
        seasonNumber: Int
    ): Flow<Resource<List<Episode>>> {
        return flow {
            emit(Resource.Loading(true))

            val episodeList: EpisodesListDto = try {
                seriesApi.getEpisodes(seriesId = seriesId, seasonNo = seasonNumber)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows due to network issues"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows due to server issues"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "An unexpected error occurred"))
                return@flow
            }

            //get watched episodes from db
            val watchedEpisodeIds = try {
                val response = client.from("UserEpisodeProgress")
                    .select(Columns.list("episode_id")) {
                        filter {
                            and {
                                //SeriesProgress::userId eq "123"
                                SeriesProgress::seriesId eq seriesId
                                SeriesProgress::seasonNo eq seasonNumber
                            }
                        }
                    }
                    .decodeList<Map<String, Int>>() // Decode as a list of maps

                // Extract the "series_id" values into a list of integers
                response.map {
                    it["episode_id"] ?: -1
                } // Replace -1 with a meaningful default if needed
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }


            val epList = episodeList.episodes?.map { dto ->
                val episode = dto.toEpisode()
                episode.watched = watchedEpisodeIds.contains(episode.id)

                // Set isReleased flag based on air date
                val airDate = dto.air_date?.let { LocalDate.parse(it) }
                val currentDate = LocalDate.now()
                episode.isReleased = airDate != null && airDate.isBefore(currentDate)

                episode
            }



            emit(Resource.Success(epList))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun addToWatched(
        episodeId: Int, seriesId: Int, seasonNo: Int, seriesName: String,
        posterPath: String
    ): Result<Unit> {
        return try {
            client.from("UserEpisodeProgress").upsert(
//                mapOf(
//                    "episodeId" to episodeId,
//                    "seriesId" to seriesId,
//                    "seasonNo" to seasonNo
//                )

                SeriesProgress(
                    //userId = "123",
                    episodeId = episodeId,
                    seriesId = seriesId,
                    seasonNo = seasonNo
                )
            )

            //also add series to watchlater so that it can be used to retrieve upcoming episodes
            client.from("WatchLater").upsert(
//                mapOf(
//                    "seriesId" to seriesId,
//                    "seriesName" to seriesName,
//                    "posterPath" to posterPath
//                )

                WatchLaterSeries(
                    //userId = "123",
                    seriesId = seriesId,
                    seriesName = seriesName,
                    posterPath = posterPath
                )
            )

            //change value in room
            seriesDatabase.seriesDao.addToWatchLater(seriesId, client.auth.currentUserOrNull()?.id.toString())

            Result.success(Unit)  // Explicitly return success

        } catch (e: Exception) {

            Result.failure(e)  // Explicitly return failure
        }
    }

    override suspend fun removeFromWatched(seriesId: Int, episodeId: Int): Result<Unit> {
        return try {
            client.from("UserEpisodeProgress").delete {
                filter {
                    and {
                        SeriesProgress::episodeId eq episodeId
                        //SeriesProgress::userId eq "123"
                    }

                }
            }


//            val watchedSeries =
//                client.postgrest["UserEpisodeProgress"].select {
//                    eq("user_id", "123")
//                    eq("series_id", seriesId)
//
//                }.decodeList<SeriesProgress>()
//
//            if (watchedSeries.isEmpty()) {
//                client.postgrest["WatchLater"].delete {
//                    WatchLaterSeries::seriesId eq seriesId
//                }

            //change value in room
            //seriesDatabase.seriesDao.removeFromWatchLater(seriesId)
            //}

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("SaveWatchLater123", "Error during insertion: ${e.message}")
            Result.failure(e)  // Explicitly return failure
        }
    }

}