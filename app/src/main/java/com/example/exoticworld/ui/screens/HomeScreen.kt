package com.example.exoticworld.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.exoticworld.R
import com.example.exoticworld.data.model.Animalitos
import com.example.exoticworld.ui.theme.shimmerEffect
import com.example.exoticworld.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel, onItemClick: (Int) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var cartItemCount by remember { mutableStateOf(3) }

    var isLoading by remember { mutableStateOf(true) }
    //Simula un delay al cargar
    LaunchedEffect(Unit) {
        delay(1000) //Duracion de la animacion shimmer
        isLoading = false //Termina la animacion
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
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
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
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            if (isLoading) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .shimmerEffect(true, 600.dp, 600.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {}
                }
            } else {
                // Search Bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar productos...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Limpiar"
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp).height(120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🔥OFERTAS ESPECIALES🔥",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Aprovecha nuestras nuevas ofertas de Halloween",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descubre a tu Mascota",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )


                Spacer(modifier = Modifier.height(8.dp))

                //Aqui van mis animalitos :D
                val animalitos = viewModel.animalitos.collectAsState()

                LazyColumn(contentPadding = PaddingValues(8.dp)) {
                    items(animalitos.value) { animalito ->
                        AnimalitosRow(
                            animalito = animalito,
                            onClick = { onItemClick(animalito.id) })
                        Divider()
                    }
                }
            }
        }
    }
}
@Composable
fun AnimalitosRow(animalito : Animalitos, onClick : () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{onClick()}
            .padding(12.dp)
    ) {
        Text(animalito.nombre, color = Color(0xFF000000)) //animalito.title me sale error mas bien en .title
        Spacer(modifier = Modifier.height(4.dp))
        Text(animalito.descripción, style = MaterialTheme.typography.bodyMedium, maxLines = 1) // lo mismo que arriba pero en .description
    }
}