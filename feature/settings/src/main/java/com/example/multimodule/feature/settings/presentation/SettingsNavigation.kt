package com.example.multimodule.feature.settings.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val SETTINGS_ROUTE = "settings_route"

fun NavGraphBuilder.settingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    composable(route = SETTINGS_ROUTE) {
        val viewModel: SettingsViewModel = hiltViewModel()
        SettingsScreen(
            onBack = onBack,
            onLogout = onLogout,
            viewModel = viewModel
        )
    }
}
