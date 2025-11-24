package com.example.exoticworld

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exoticworld.ui.viewmodel.AddViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(vm: AddViewModel = viewModel()) {
    val state = vm.formState.collectAsState().value

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold (
        topBar = { TopAppBar(title = { Text("Perfil") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column (modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp) )
        {
            // Campo nombre
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { vm.onNombreChange(it) },
                label = { Text("Nombre de usuario") },
                isError = state.nombreError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.nombreError != null) {
                Text(
                    text = state.nombreError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Campo correoUsuario
            OutlinedTextField(
                value = state.correoUsuario,
                onValueChange = { vm.onCorreoChange(it) },
                label = { Text("Correo") },
                isError = state.correoUsuarioError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.correoUsuarioError != null) {
                Text(
                    text = state.correoUsuarioError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Campo contrasena
            OutlinedTextField(
                value = state.contrasena,
                onValueChange = { vm.onContrasenaChange(it) },
                label = { Text("Contraseña") },
                isError = state.contrasenaError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.contrasenaError != null) {
                Text(
                    text = state.contrasenaError ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botón de guardar habilitado solo si el form es válido
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Usuario guardado con exito")
                    }
                    vm.onLoginSubmit() },
                enabled = state.isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}

@Composable
fun MainProfile(){
    Card(){
        Icons.Default.Person
        Text("Hola")

    }
}
