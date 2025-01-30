package com.nextflix.app.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.nextflix.app.api.SeriesApi
import com.nextflix.app.authentication.data.repository.AuthenticationRepositoryImpl
import com.nextflix.app.authentication.domain.repository.AuthenticationRepository
import com.nextflix.app.authentication.utils.SharedPreferenceHelper
import com.nextflix.app.seriesList.data.local.series.SeriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    @Singleton
    fun providesSeriesApi(): SeriesApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(SeriesApi.BASE_URL)
            .client(client)
            .build()
            .create(SeriesApi::class.java)
    }

    @Provides
    @Singleton
    fun providesSeriesDatabase(app: Application): SeriesDatabase {
        return Room.databaseBuilder(
            app,
            SeriesDatabase::class.java,
            "series.db"
        ).build()
    }

    @Provides
    fun provideSharedPreferenceHelper(@ApplicationContext context: Context): SharedPreferenceHelper {
        return SharedPreferenceHelper(context)
    }

//    @Provides
//    @Singleton
//    fun provideAuthenticationRepository(
//        sharedPref: SharedPreferenceHelper,
//        @ApplicationContext context: Context
//    ): AuthenticationRepository {
//        return AuthenticationRepositoryImpl(sharedPref, context)
//    }
}