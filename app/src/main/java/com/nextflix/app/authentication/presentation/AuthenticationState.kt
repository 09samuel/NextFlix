package com.nextflix.app.authentication.presentation

data class AuthenticationState (
    val isLoading: Boolean = false,
    val error: String? = "",
    val accessToken: String? = "",
    val userId: String? = null,
    val isLogin: Boolean = false,
)