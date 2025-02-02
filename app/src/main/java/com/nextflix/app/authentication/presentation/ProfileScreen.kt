package com.nextflix.app.authentication.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.github.jan.supabase.annotations.SupabaseExperimental

@SuppressLint("SuspiciousIndentation")

@Composable
fun ProfileScreen(
    authenticationViewModel: AuthenticationViewModel,
    onEvent: (AuthenticationUiEvent) -> Unit,
    navController: NavHostController
) {

    // Observe authentication state from the ViewModel
    val authenticationState = authenticationViewModel.authenticationState.collectAsState()

    LaunchedEffect(authenticationState.value.isLogin) {
        if (!authenticationState.value.isLogin) {
            navController.navigate("login_screen") {
                popUpTo(0) { inclusive = true } // Clears all previous screens
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .alpha(if (authenticationState.value.isLoading) 0.5f else 1f),
    ) {
        Button(
            onClick = { onEvent(AuthenticationUiEvent.OnLogoutClicked) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 32.dp),
            enabled = !authenticationState.value.isLoading

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (authenticationState.value.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp),
                        color = Color.Gray,
                        strokeWidth = 2.dp
                    )
                } else {


                    // Text
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

            }
        }
    }
}

