package com.nextflix.app.authentication.data.repository

//import io.github.jan.supabase.gotrue.gotrue

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.nextflix.app.BuildConfig
import com.nextflix.app.authentication.domain.repository.AuthenticationRepository
import com.nextflix.app.authentication.utils.SharedPreferenceHelper
import com.nextflix.app.core.network.SupabaseClient.client
import com.nextflix.app.seriesList.utils.Resource
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPreferenceHelper,
) : AuthenticationRepository {

    override suspend fun saveToken(): Result<Unit> {
        return try {
            val accessToken = client.auth.currentAccessTokenOrNull()
            sharedPref.saveStringData("accessToken", accessToken)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getToken(): Flow<Resource<String?>> {
        return flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(sharedPref.getStringData("accessToken")))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            
            client.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(context: Context): Result<Unit> {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val credentialManager = CredentialManager.create(context)

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.googleClientId)
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()


        return try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            val credential = result.credential

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)

            val googleIdToken = googleIdTokenCredential.idToken

            val session = client.auth.signInWith(IDToken) {
                idToken = googleIdToken
                provider = Google
                nonce = rawNonce
            }

            //Toast.makeText(context, "You are signed in", Toast.LENGTH_SHORT).show()
            Result.success(Unit)
        } catch (e: GetCredentialException) {
            //Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Result.failure(e)
        } catch (e: GoogleIdTokenParsingException) {
            //Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            Result.failure(e)
        }
    }
}