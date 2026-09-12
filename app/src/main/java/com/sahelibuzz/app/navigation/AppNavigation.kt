package com.sahelibuzz.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.sahelibuzz.app.ui.home.HomeScreen

object AppRoutes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val NOTIFICATIONS = "notifications"
    const val PROFILE = "profile"
    const val CHAT = "chat"
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: String
)

private val bottomNavItems = listOf(
    BottomNavItem(
        route = AppRoutes.HOME,
        label = "Home",
        icon = "🏠"
    ),
    BottomNavItem(
        route = AppRoutes.SEARCH,
        label = "Search",
        icon = "🔍"
    ),
    BottomNavItem(
        route = AppRoutes.NOTIFICATIONS,
        label = "Alerts",
        icon = "🔔"
    ),
    BottomNavItem(
        route = AppRoutes.CHAT,
        label = "Chat",
        icon = "💬"
    ),
    BottomNavItem(
        route = AppRoutes.PROFILE,
        label = "Profile",
        icon = "👤"
    )
)

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->

                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(AppRoutes.HOME) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Text(text = item.icon)
                        },
                        label = {
                            Text(text = item.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = AppRoutes.HOME,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(AppRoutes.HOME) {
                HomeScreen(
                    contentPadding = innerPadding
                )
            }

            composable(AppRoutes.SEARCH) {
                PlaceholderScreen(
                    title = "Search",
                    contentPadding = innerPadding
                )
            }

            composable(AppRoutes.NOTIFICATIONS) {
                PlaceholderScreen(
                    title = "Notifications",
                    contentPadding = innerPadding
                )
            }

            composable(AppRoutes.PROFILE) {
                PlaceholderScreen(
                    title = "Profile",
                    contentPadding = innerPadding
                )
            }

            composable(AppRoutes.CHAT) {
                PlaceholderScreen(
                    title = "Chat",
                    contentPadding = innerPadding
                )
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    contentPadding: PaddingValues
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}
