package com.sahelibuzz.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sahelibuzz.app.ui.home.HomeScreen

object AppRoutes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val NOTIFICATIONS = "notifications"
    const val PROFILE = "profile"
    const val CHAT = "chat"
}

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME
    ) {
        composable(AppRoutes.HOME) {
            HomeScreen()
        }

        composable(AppRoutes.SEARCH) {
            PlaceholderScreen(title = "Search")
        }

        composable(AppRoutes.NOTIFICATIONS) {
            PlaceholderScreen(title = "Notifications")
        }

        composable(AppRoutes.PROFILE) {
            PlaceholderScreen(title = "Profile")
        }

        composable(AppRoutes.CHAT) {
            PlaceholderScreen(title = "Chat")
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title)
    }
}
