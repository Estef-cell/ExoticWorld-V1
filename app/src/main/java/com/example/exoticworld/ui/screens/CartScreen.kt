package com.example.exoticworld.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exoticworld.data.model.CartItem
import com.example.exoticworld.ui.viewmodel.MainViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.exoticworld.ui.theme.shimmerEffect
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: MainViewModel) {
    val items = viewModel.cart.collectAsState().value
    val total = items.sumOf { it.subtotal }
    var isLoading by remember { mutableStateOf(true) }
    //Simula un delay al cargar
    LaunchedEffect (Unit) {
        delay(1000) //Duracion de la animacion shimmer
        isLoading = false //Termina la animacion

    }


    Scaffold(

        topBar = { TopAppBar(title = { Text("Carrito") }) },
        bottomBar = {
            if (items.isNotEmpty()) {
                Surface {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: $$total", style = MaterialTheme.typography.titleMedium)
                        Button(onClick = { /* flujo de pago */ }) { Text("Pagar") }
                    }
                }
            }
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
        if (items.isEmpty()) {
            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(12.dp)
            ) {
                items(items, key = { it.producto.id }) { ci ->
                    CartRow(
                        item = ci,
                        onDec = { viewModel.decFromCart(ci.producto.id) },
                        onInc = { viewModel.addToCart(ci.producto) },
                        onRemove = { viewModel.removeFromCart(ci.producto.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartRow(item: CartItem, onDec: () -> Unit, onInc: () -> Unit, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Text(item.producto.nombre, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("$${item.producto.precio} c/u", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onDec) { Icon(Icons.Filled.Remove, contentDescription = "Menos") }
                Text(item.quantity.toString(), style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onInc) { Icon(Icons.Filled.Add, contentDescription = "Más") }
                Spacer(Modifier.weight(1f))
                Text("Subtotal: $${item.subtotal}", style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = onRemove) { Icon(Icons.Filled.Delete, contentDescription = "Quitar") }
            }
        }
    }
}
