package com.example.multimodule.feature.dashboard.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val DASHBOARD_ROUTE = "dashboard_route"
const val TASK_LIST_ROUTE = "task_list_route"

fun NavGraphBuilder.dashboardScreen(
    onNavigateToTaskList: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    composable(route = DASHBOARD_ROUTE) {
        val viewModel: DashboardViewModel = hiltViewModel()
        DashboardScreen(
            onNavigateToTaskList = onNavigateToTaskList,
            onNavigateToSettings = onNavigateToSettings,
            viewModel = viewModel
        )
    }
}

fun NavGraphBuilder.taskListScreen(
    onBack: () -> Unit
) {
    composable(
        route = "$TASK_LIST_ROUTE/{listId}",
        arguments = listOf(
            navArgument("listId") { type = NavType.StringType }
        )
    ) {
        val viewModel: TaskListViewModel = hiltViewModel()
        TaskListScreen(
            onBack = onBack,
            viewModel = viewModel
        )
    }
}
