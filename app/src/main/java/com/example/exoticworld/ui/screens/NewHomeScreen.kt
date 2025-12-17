package com.example.exoticworld.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.exoticworld.R
import com.example.exoticworld.data.model.ProductoModel
import com.example.exoticworld.data.model.UiState
import com.example.exoticworld.ui.viewmodel.ProductoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Pantalla principal con búsqueda y listado de productos desde la API
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewHomeScreen(
    viewModel: ProductoViewModel,
    onItemClick: (Int) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val productosState by viewModel.productosState.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Mostrar errores con Snackbar
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limpiarErrorMessage()
        }
    }

    // Búsqueda con debounce
    LaunchedEffect(searchText) {
        if (searchText.isNotBlank()) {
            delay(500) // Debounce de 500ms
            viewModel.buscarProductosPorNombre(searchText)
        } else {
            viewModel.limpiarBusqueda()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_exoticworld),
                            contentDescription = "Logo ExoticWorld",
                            modifier = Modifier.size(48.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Bienvenido a",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "ExoticWorld",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Buscar productos por nombre...") },
                leadingIcon = { Icon(Icons.Default.Search, "Buscar") },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(Icons.Default.Clear, "Limpiar")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Banner promocional
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "OFERTAS ESPECIALES",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Productos desde la API backend",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (searchText.isBlank()) "Todos los Productos" else "Resultados de búsqueda",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar productos según búsqueda o listado completo
            val currentState = if (searchText.isBlank()) productosState else searchState

            when (currentState) {
                is UiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Cargando productos...")
                    }
                }
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val productos = currentState.data
                    if (productos.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No se encontraron productos")
                        }
                    } else {
                        LazyColumn(contentPadding = PaddingValues(8.dp)) {
                            items(productos, key = { it.productoId }) { producto ->
                                ProductoRowItem(
                                    producto = producto,
                                    onClick = { onItemClick(producto.productoId) },
                                    onAddToCart = {
                                        viewModel.agregarProductoAlCarrito(producto.productoId)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Producto agregado al carrito")
                                        }
                                    }
                                )
                                Divider()
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: ${currentState.message}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.cargarProductos() }) {
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
fun ProductoRowItem(
    producto: ProductoModel,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    producto.nombreProducto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    producto.descripcionProducto,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "$${producto.precioProducto}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onAddToCart) {
                Text("+")
            }
        }
    }
}
