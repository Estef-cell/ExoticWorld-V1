package com.example.exoticworld.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.exoticworld.data.model.CarritoItemModel
import com.example.exoticworld.data.model.UiState
import com.example.exoticworld.ui.viewmodel.ProductoViewModel

/**
 * Pantalla del carrito conectada a la API
 * Muestra items, total y permite modificar cantidades
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCartScreen(viewModel: ProductoViewModel) {
    val carritoState by viewModel.carritoState.collectAsState()
    val totalCarrito by viewModel.totalCarrito.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar errores
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limpiarErrorMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                actions = {
                    if (carritoState is UiState.Success && (carritoState as UiState.Success<List<CarritoItemModel>>).data.isNotEmpty()) {
                        TextButton(onClick = { viewModel.vaciarCarrito() }) {
                            Text("Vaciar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (carritoState is UiState.Success) {
                val items = (carritoState as UiState.Success<List<CarritoItemModel>>).data
                if (items.isNotEmpty()) {
                    Surface(
                        shadowElevation = 8.dp,
                        tonalElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Total:",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "$${String.format("%.2f", totalCarrito)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Button(
                                onClick = {
                                    // Flujo de pago
                                }
                            ) {
                                Text("Proceder al Pago")
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (carritoState) {
                is UiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Inicializando carrito...")
                    }
                }
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val items = (carritoState as UiState.Success<List<CarritoItemModel>>).data
                    if (items.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Tu carrito está vacío",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Agrega productos para comenzar",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(items, key = { it.itemId }) { item ->
                                NewCartRow(
                                    item = item,
                                    onDec = {
                                        viewModel.decrementarProductoEnCarrito(item.producto.productoId)
                                    },
                                    onInc = {
                                        viewModel.agregarProductoAlCarrito(item.producto.productoId)
                                    },
                                    onRemove = {
                                        viewModel.eliminarProductoDelCarrito(item.producto.productoId)
                                    }
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: ${(carritoState as UiState.Error).message}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.cargarCarrito() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NewCartRow(
    item: CarritoItemModel,
    onDec: () -> Unit,
    onInc: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    item.producto.nombreProducto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onRemove) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Text(
                item.producto.descripcionProducto,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            Spacer(Modifier.height(4.dp))

            Text(
                "$${item.producto.precioProducto} c/u",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onDec) {
                        Icon(Icons.Filled.Remove, contentDescription = "Decrementar")
                    }
                    Text(
                        item.cantidad.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onInc) {
                        Icon(Icons.Filled.Add, contentDescription = "Incrementar")
                    }
                }

                Text(
                    "Subtotal: $${String.format("%.2f", item.subtotal)}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
