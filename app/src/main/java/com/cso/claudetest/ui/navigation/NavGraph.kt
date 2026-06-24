package com.cso.claudetest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cso.claudetest.ui.detail.TodoDetailScreen
import com.cso.claudetest.ui.list.TodoListScreen

@Composable
fun TodoNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.TodoList.route) {

        composable(Screen.TodoList.route) {
            TodoListScreen(
                onNavigateToDetail = { todoId ->
                    navController.navigate(Screen.TodoDetail.createRoute(todoId))
                }
            )
        }

        composable(
            route = Screen.TodoDetail.route,
            arguments = listOf(
                navArgument(Screen.TodoDetail.ARG_TODO_ID) { type = NavType.LongType }
            )
        ) {
            TodoDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
