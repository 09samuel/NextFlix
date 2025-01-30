package com.nextflix.app.authentication.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextflix.app.authentication.domain.repository.AuthenticationRepository
import com.nextflix.app.seriesList.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {
    private var _authenticationState = MutableStateFlow(AuthenticationState())
    val authenticationState = _authenticationState.asStateFlow()

    init{
        isUserLoggedIn()
    }

    fun onEvent(event: AuthenticationUiEvent) {
        when (event) {
            is AuthenticationUiEvent.OnLoginWithGoogleClicked -> {
                loginWithGoogle(event.context)
            }

            AuthenticationUiEvent.OnLaunch -> {
                isUserLoggedIn()
            }

            AuthenticationUiEvent.OnLogoutClicked -> {
                logout()
            }
        }
    }

    private fun loginWithGoogle(context: Context) {
        viewModelScope.launch {
            _authenticationState.update {
                it.copy(isLoading = true)
            }

            val logInResult =
                authenticationRepository.loginWithGoogle(context = context)
            if (logInResult.isSuccess) {
                val tokenResult = authenticationRepository.saveToken()
                if (tokenResult.isSuccess) {
                    _authenticationState.update {
                        it.copy(isLoading = false, error = null, isLogin = true)
                    }
                }  else{
                    _authenticationState.update {
                        it.copy(isLoading = false, error = "Falied to sign in", isLogin = true)
                    }
                }

            } else {
                _authenticationState.update {
                    it.copy(isLoading = false, error = "Failed to sign in")
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _authenticationState.update {
                it.copy(isLoading = true)
            }

            val logOutResult = authenticationRepository.logout()
            if (logOutResult.isSuccess) {
                _authenticationState.update {
                    authenticationRepository.saveToken()
                    it.copy(isLoading = false, error = null, isLogin = false)
                }
            } else {
                _authenticationState.update {
                    it.copy(isLoading = false, error = "Failed to sign in")
                }
            }
        }
    }

    private fun isUserLoggedIn() {
        viewModelScope.launch {
            _authenticationState.update {
                it.copy(isLoading = true)
            }

            authenticationRepository.getToken().collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _authenticationState.update {
                            it.copy(isLoading = false, error = result.message)
                        }
                    }

                    is Resource.Loading -> {
                        _authenticationState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { token ->
                            _authenticationState.update {
                                it.copy(accessToken = token, isLogin = true, )
                            }
                        }
                    }
                }
            }
        }
    }
}

