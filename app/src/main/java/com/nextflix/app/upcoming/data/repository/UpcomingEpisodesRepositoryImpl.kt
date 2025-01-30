package com.nextflix.app.upcoming.data.repository

import coil.network.HttpException
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.core.network.SupabaseClient.client
import com.nextflix.app.seriesDetails.data.respond.episodes.EpisodesListDto
import com.nextflix.app.seriesDetails.data.respond.generalDetails.GeneralDetailsDto
import com.nextflix.app.seriesList.utils.Resource
import com.nextflix.app.upcoming.data.mappers.toUpcomingEpisode
import com.nextflix.app.upcoming.domain.model.UpcomingEpisode
import com.nextflix.app.upcoming.domain.repository.UpcomingEpisodesRepository
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class UpcomingEpisodesRepositoryImpl @Inject constructor(
    private val seriesApi: SeriesApi,
) : UpcomingEpisodesRepository {
    override suspend fun getUpcomingEpisodesList(): Flow<Resource<List<UpcomingEpisode>>> {
        return flow {
            emit(Resource.Loading(true))

            // Get all IDs of series in watch later
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

            val upcomingEpisodeList = mutableListOf<UpcomingEpisode>()
            val currentDate = LocalDate.now()

            for (seriesId in watchLaterSeriesIds) {
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

                val noOfSeasons = details.number_of_seasons ?: 0
                val seriesName = details.name ?: ""

                if (noOfSeasons != 0) {
                    val episodeList: EpisodesListDto = try {
                        seriesApi.getEpisodes(seriesId = seriesId, seasonNo = noOfSeasons)
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

                    episodeList.episodes
                        ?.filter { dto ->
                            val airDate = dto.air_date?.let { LocalDate.parse(it) }
                            airDate == null || airDate.isAfter(currentDate)
                        }
                        ?.map { dto ->
                            val airDate = dto.air_date?.let { LocalDate.parse(it) }
                            val daysLeft =
                                airDate?.let { ChronoUnit.DAYS.between(currentDate, it).toInt() }
                                    ?: Int.MAX_VALUE
                            dto.toUpcomingEpisode().copy(
                                series_id = seriesId,
                                series_name = seriesName,
                                daysLeft = daysLeft,

                                )
                        }
                        ?.let { upcomingEpisodeList.addAll(it) }


                }
            }

            // Sort the list by ascending order of days left
            val sortedUpcomingEpisodes = upcomingEpisodeList.sortedBy { it.daysLeft }

            emit(Resource.Success(data = sortedUpcomingEpisodes))
            emit(Resource.Loading(false))
        }
    }
}
