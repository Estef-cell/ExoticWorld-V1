package com.example.exoticworld.navegation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem(Routes.HOME, "Home", Icons.Default.Home)
    object Profile : BottomNavItem(Routes.PROFILE, "Perfiles", Icons.Default.Person)
    object Cart : BottomNavItem(Routes.CART, "carrito", Icons.Default.ShoppingCart)
    object Settings : BottomNavItem(Routes.SETTINGS, "configuración", Icons.Default.Settings)
}

@Composable
fun BottomBar(navController: NavHostController, items: List<BottomNavItem>, cartCount: Int) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val iconComposable: @Composable () -> Unit = {
                if (item is BottomNavItem.Cart && cartCount > 0) {
                    BadgedBox(badge = { Badge { Text(cartCount.toString()) } }) {
                        Icon(item.icon, contentDescription = item.label)
                    }
                } else {
                    Icon(item.icon, contentDescription = item.label)
                }
            }
            NavigationBarItem(
                icon = iconComposable,
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationRoute ?: Routes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
