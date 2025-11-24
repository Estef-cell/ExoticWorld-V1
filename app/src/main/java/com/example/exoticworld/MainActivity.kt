package com.example.exoticworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.exoticworld.navegation.BottomBar
import com.example.exoticworld.navegation.BottomNavItem
import com.example.exoticworld.navegation.Routes
import com.example.exoticworld.ui.screens.CartScreen
import com.example.exoticworld.ui.screens.CategoryScreen
import com.example.exoticworld.ui.screens.HomeScreen
import com.example.exoticworld.ui.screens.ProfileScreen
import com.example.exoticworld.ui.screens.SettingsScreen
import com.example.exoticworld.ui.theme.ExoticWorldTheme
import com.example.exoticworld.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { ExoticWorldTheme { App() } }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val vm: MainViewModel = viewModel()

    val bottomItems = listOf(BottomNavItem.Home, BottomNavItem.Profile, BottomNavItem.Cart, BottomNavItem.Settings)
    val cartCount = vm.cart.collectAsState().value.sumOf { it.quantity }

    Scaffold(bottomBar = { BottomBar(navController, bottomItems, cartCount) }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(viewModel = vm, onItemClick = { id -> navController.navigate(Routes.category(id)) })
            }
            composable(Routes.PROFILE) { ProfileScreen() }
            composable(Routes.CART) { CartScreen(viewModel = vm) }
            composable(Routes.SETTINGS) { SettingsScreen() }
            composable(
                route = Routes.CATEGORY,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStack ->
                val id = backStack.arguments?.getInt("id") ?: -1
                CategoryScreen(id = id, viewModel = vm, onBack = { navController.popBackStack() })
            }
        }
    }
}
