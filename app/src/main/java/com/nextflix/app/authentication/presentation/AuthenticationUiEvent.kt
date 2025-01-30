package com.nextflix.app.authentication.presentation

import android.content.Context

sealed interface AuthenticationUiEvent {
    data class OnLoginWithGoogleClicked(val context: Context) : AuthenticationUiEvent
    object OnLaunch : AuthenticationUiEvent
    object OnLogoutClicked : AuthenticationUiEvent
}