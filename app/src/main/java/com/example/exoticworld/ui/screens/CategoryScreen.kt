package com.example.exoticworld.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.exoticworld.data.model.Productos
import com.example.exoticworld.ui.viewmodel.MainViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.exoticworld.ui.theme.shimmerEffect
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(id: Int, viewModel: MainViewModel, onBack: () -> Unit) {
    val titulo = viewModel.getAnimalito(id)?.nombre ?: "Categoría #$id"
    val list = viewModel.productosPorCategoria(id)
    var isLoading by remember { mutableStateOf(true) }
    //Simula un delay al cargar
    LaunchedEffect(Unit) {
        delay(1000) //Duracion de la animacion shimmer
        isLoading = false //Termina la animacion

    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás") }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .shimmerEffect(true, 900.dp, 900.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {}
            }
        } else
        if (list.isEmpty()) {
            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Sin productos para $titulo")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(list, key = { it.id }) { p ->
                    ProductoRow(p, onAdd = { viewModel.addToCart(p) })
                }
            }
        }
    }
}

@Composable
private fun ProductoRow(p: Productos, onAdd: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Text(p.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(p.descripcion, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text("$${p.precio}", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onAdd) { Text("Añadir al carrito") }
        }
    }
}
