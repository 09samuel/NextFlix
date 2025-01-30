package com.nextflix.app.di

import com.nextflix.app.authentication.data.repository.AuthenticationRepositoryImpl
import com.nextflix.app.authentication.domain.repository.AuthenticationRepository
import com.nextflix.app.seriesDetails.data.repository.SeriesDetailsRepositoryImpl
import com.nextflix.app.seriesDetails.domain.repository.SeriesDetailsRepository
import com.nextflix.app.seriesList.data.repository.SeriesListRepositoryImpl
import com.nextflix.app.seriesList.domain.repository.SeriesListRepository
import com.nextflix.app.upcoming.data.repository.UpcomingEpisodesRepositoryImpl
import com.nextflix.app.upcoming.domain.repository.UpcomingEpisodesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSeriesListRepository(
        seriesListRepositoryImpl: SeriesListRepositoryImpl
    ): SeriesListRepository

    @Binds
    @Singleton
    abstract fun bindSeriesDetailsRepository(
        seriesDetailsRepositoryImpl: SeriesDetailsRepositoryImpl
    ): SeriesDetailsRepository

    @Binds
    @Singleton
    abstract fun bindUpcomingEpisodesRepository(
        upcomingEpisodesRepositoryImpl: UpcomingEpisodesRepositoryImpl
    ): UpcomingEpisodesRepository

    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository
}