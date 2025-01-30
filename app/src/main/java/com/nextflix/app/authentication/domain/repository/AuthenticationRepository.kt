package com.nextflix.app.authentication.domain.repository

import android.content.Context
import com.nextflix.app.seriesList.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun saveToken(): Result<Unit>
    suspend fun getToken(): Flow<Resource<String?>>
    suspend fun logout(): Result<Unit>
    suspend fun loginWithGoogle(context: Context): Result<Unit>
}

