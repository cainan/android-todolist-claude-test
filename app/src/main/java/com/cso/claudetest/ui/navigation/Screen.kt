package com.cso.claudetest.ui.navigation

sealed class Screen(val route: String) {
    data object TodoList : Screen("todo_list")
    data object TodoDetail : Screen("todo_detail/{todoId}") {
        fun createRoute(id: Long) = "todo_detail/$id"
        const val ARG_TODO_ID = "todoId"
    }
}
