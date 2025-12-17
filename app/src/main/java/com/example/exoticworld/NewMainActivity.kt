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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.exoticworld.navegation.BottomBar
import com.example.exoticworld.navegation.BottomNavItem
import com.example.exoticworld.navegation.Routes
import com.example.exoticworld.ui.screens.*
import com.example.exoticworld.ui.theme.ExoticWorldTheme
import com.example.exoticworld.ui.viewmodel.ProductoViewModel

/**
 * Activity principal de la app
 * Aca arranca todo cuando abro la aplicacion
 */
class NewMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Para que la app use toda la pantalla
        setContent {
            ExoticWorldTheme {  // Aplico el tema de colores de la app
                AppWithApi()     // Llamo a la funcion que tiene toda la navegacion
            }
        }
    }
}

/**
 * Funcion principal que maneja la navegacion de la app
 * Aca defino todas las pantallas y el ViewModel
 */
@Composable
fun AppWithApi() {
    // Controlador para navegar entre pantallas
    val navController = rememberNavController()
    val context = LocalContext.current

    // Creo el ViewModel que maneja toda la logica de productos y carrito
    // Este ViewModel se conecta con la API del backend
    val vm: ProductoViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    // Botones del menu inferior (Home, Perfil, Carrito, Configuracion)
    val bottomItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Cart,
        BottomNavItem.Settings
    )

    // Cuento cuantos productos hay en el carrito para mostrar el numerito
    val carritoState = vm.carritoState.collectAsState()
    val cartCount = when (val state = carritoState.value) {
        is com.example.exoticworld.data.model.UiState.Success -> {
            state.data.sumOf { it.cantidad }  // Sumo todas las cantidades
        }
        else -> 0  // Si no hay nada o esta cargando, muestro 0
    }

    Scaffold(
        bottomBar = { BottomBar(navController, bottomItems, cartCount) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home con productos de la API
            composable(Routes.HOME) {
                NewHomeScreen(
                    viewModel = vm,
                    onItemClick = { id ->
                        navController.navigate(Routes.productDetail(id))
                    }
                )
            }

            // Detalle de producto
            composable(
                route = Routes.PRODUCT_DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStack ->
                val productoId = backStack.arguments?.getInt("id") ?: -1
                ProductDetailScreen(
                    productoId = productoId,
                    viewModel = vm,
                    onBack = { navController.popBackStack() }
                )
            }

            // Carrito conectado a la API
            composable(Routes.CART) {
                NewCartScreen(viewModel = vm)
            }

            // Perfil y Settings (mantener las existentes)
            composable(Routes.PROFILE) { ProfileScreen() }
            composable(Routes.SETTINGS) { SettingsScreen() }

            // Categoría (mantener si aún usas Animalitos)
            composable(
                route = Routes.CATEGORY,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStack ->
                val id = backStack.arguments?.getInt("id") ?: -1
                // Aquí puedes mantener CategoryScreen si la necesitas
                // o redirigir a detalle de producto
                NewHomeScreen(
                    viewModel = vm,
                    onItemClick = { productId ->
                        navController.navigate(Routes.productDetail(productId))
                    }
                )
            }
        }
    }
}
