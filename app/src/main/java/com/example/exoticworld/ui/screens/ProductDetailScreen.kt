package com.example.exoticworld.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.exoticworld.data.model.ProductoModel
import com.example.exoticworld.data.model.UiState
import com.example.exoticworld.data.remote.ApiClient
import com.example.exoticworld.data.repository.ProductoRepository
import com.example.exoticworld.ui.viewmodel.ProductoViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla de detalle de producto
 * Muestra información completa y permite agregar al carrito
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productoId: Int,
    viewModel: ProductoViewModel,
    onBack: () -> Unit
) {
    val repository = remember { ProductoRepository(ApiClient.service) }
    var uiState by remember { mutableStateOf<UiState<ProductoModel>>(UiState.Loading) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(productoId) {
        uiState = UiState.Loading
        val result = repository.getProductoById(productoId)
        uiState = result.fold(
            onSuccess = { UiState.Success(it) },
            onFailure = { UiState.Error(it.message ?: "Error al cargar producto") }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val state = uiState) {
                is UiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Cargando...")
                    }
                }
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val producto = state.data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = producto.nombreProducto,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Divider()

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Descripción:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = producto.descripcionProducto,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Divider()

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Precio:",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = "$${producto.precioProducto}",
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.agregarProductoAlCarrito(producto.productoId)
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Producto agregado al carrito")
                                            }
                                        },
                                        modifier = Modifier.height(56.dp)
                                    ) {
                                        Text("Agregar al Carrito")
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Información adicional",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "• Envío disponible a todo el país",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "• Garantía de 30 días",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "• Atención al cliente 24/7",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: ${state.message}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                scope.launch {
                                    uiState = UiState.Loading
                                    val result = repository.getProductoById(productoId)
                                    uiState = result.fold(
                                        onSuccess = { UiState.Success(it) },
                                        onFailure = { UiState.Error(it.message ?: "Error") }
                                    )
                                }
                            }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}
