package com.example.multimodule.feature.auth.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val AUTH_ROUTE = "auth_route"

fun NavGraphBuilder.authScreen(
    onNavigateToDashboard: () -> Unit
) {
    composable(route = AUTH_ROUTE) {
        val viewModel: AuthViewModel = hiltViewModel()
        AuthScreen(
            onSuccess = onNavigateToDashboard,
            viewModel = viewModel
        )
    }
}
