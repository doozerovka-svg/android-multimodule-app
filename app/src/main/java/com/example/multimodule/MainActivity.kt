package com.example.multimodule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.multimodule.core.ui.theme.AppTheme
import com.example.multimodule.feature.auth.presentation.AUTH_ROUTE
import com.example.multimodule.feature.auth.presentation.authScreen
import com.example.multimodule.feature.dashboard.presentation.DASHBOARD_ROUTE
import com.example.multimodule.feature.dashboard.presentation.TASK_LIST_ROUTE
import com.example.multimodule.feature.dashboard.presentation.dashboardScreen
import com.example.multimodule.feature.dashboard.presentation.taskListScreen
import com.example.multimodule.feature.settings.presentation.SETTINGS_ROUTE
import com.example.multimodule.feature.settings.presentation.settingsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = AUTH_ROUTE
                    ) {
                        authScreen(
                            onNavigateToDashboard = {
                                navController.navigate(DASHBOARD_ROUTE) {
                                    popUpTo(AUTH_ROUTE) { inclusive = true }
                                }
                            }
                        )
                        dashboardScreen(
                            onNavigateToTaskList = { listId ->
                                navController.navigate("$TASK_LIST_ROUTE/$listId")
                            },
                            onNavigateToSettings = {
                                navController.navigate(SETTINGS_ROUTE)
                            }
                        )
                        taskListScreen(
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                        settingsScreen(
                            onBack = {
                                navController.popBackStack()
                            },
                            onLogout = {
                                navController.navigate(AUTH_ROUTE) {
                                    popUpTo(DASHBOARD_ROUTE) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
