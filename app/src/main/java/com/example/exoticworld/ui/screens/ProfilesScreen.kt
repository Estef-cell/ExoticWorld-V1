package com.example.exoticworld.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exoticworld.ui.theme.shimmerEffect
import com.example.exoticworld.ui.viewmodel.AddViewModel
import com.example.exoticworld.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import com.example.exoticworld.ui.components.ProfileImage
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(vm: AddViewModel = viewModel()) {
    val state = vm.formState.collectAsState().value
    var showFormSession by remember { mutableStateOf(false) }
    var showFormRegister by remember { mutableStateOf(false) }
    var showUserSignIn by remember { mutableStateOf(false) }

    val authViewModel: AuthViewModel = viewModel()
    val currentUser = authViewModel.currentUser.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    //Simula un delay al cargar
    LaunchedEffect(Unit) {
        delay(1000) //Duracion de la animacion shimmer
        isLoading = false //Termina la animacion
        authViewModel.listenAuthState() //Obtiene el estado del usuario
        if (currentUser.value != null) { //Si el usuario esta ingresado, pasa directo a la pantalla de usuario
            showUserSignIn = true
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Perfil") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
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
                            .shimmerEffect(true, 500.dp, 500.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {}
                }
            } else {
                if (!showFormSession and !showFormRegister and !showUserSignIn) {
                    // Mostrar la tarjeta con el mensaje y botones para login o registro
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.elevatedCardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Icono genérico de usuario
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Usuario",
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Mensaje de usuario no ingresado
                            Text(
                                text = "Usuario no ingresado",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Botones para login y crear cuenta
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        showFormSession = true
                                    }, // Muestra el formulario de login
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Iniciar sesión")
                                }

                                Button(
                                    onClick = {
                                        showFormRegister = true
                                    }, // Muestra el formulario de registro
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Crear cuenta")
                                }

                                Button(
                                    onClick = {
                                        authViewModel.listenAuthState()
                                        if (currentUser.value != null) { //Si el usuario esta ingresado, pasa directo a la pantalla de usuario
                                            showUserSignIn = true
                                            showFormSession = false
                                            showFormRegister = false
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Actualizar estado")
                                }
                            }
                        }
                    }
                } else if (showFormSession) {
                    // Si showFormSession es true, muestra el formulario de ingreso
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Iniciar Sesion",
                            style = MaterialTheme.typography.titleMedium
                        )

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
                                authViewModel.loginUser(state.correoUsuario,state.contrasena)
                                vm.onLoginSubmit()
                                showFormSession = false
                                if (currentUser.value != null) { //Si el usuario esta ingresado, pasa directo a la pantalla de usuario
                                    showUserSignIn = true
                                    showFormSession = false
                                }
                            },
                            enabled = state.isValidLogin,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Iniciar sesion")
                        }

                        // Botón para volver atrás
                        TextButton(
                            onClick = {
                                showFormSession = false
                                vm.onLoginSubmit()
                                      },
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Text("← Volver")
                        }
                    }
                } else if (showFormRegister) {
                    // Si showFormRegister es true, muestra el formulario de ingreso
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Registro",
                            style = MaterialTheme.typography.titleMedium
                        )

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

                        // Campo comfirmarContrasena
                        OutlinedTextField(
                            value = state.confirmarContrasena,
                            onValueChange = { vm.onConfirmarContrasenaChange(it) },
                            label = { Text("Confirmar Contrasena") },
                            isError = state.confirmarContrasenaError != null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (state.confirmarContrasenaError != null) {
                            Text(
                                text = state.confirmarContrasenaError ?: "",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // Botón de guardar habilitado solo si el form es válido
                        Button(
                            onClick = {
                                authViewModel.registerUser(state.correoUsuario,state.contrasena)
                                vm.onLoginSubmit()
                                showFormRegister = false },
                            enabled = state.isValid,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Guardar")
                        }

                        // Botón para volver atrás
                        TextButton(
                            onClick = {
                                showFormRegister = false
                                vm.onLoginSubmit()
                                      },
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Text("← Volver")
                        }
                    }
                } else if (showUserSignIn) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.elevatedCardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Icono genérico de usuario pd:ya no es generico B)
                            ProfileImage()

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Se inicio de forma correcta", //revisar como hacer que funcione
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Boton cerrar sesion
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        authViewModel.signOut()
                                        showUserSignIn = false
                                        if (currentUser.value == null) { //Si el usuario esta ingresado, pasa directo a la pantalla de usuario
                                            showUserSignIn = false
                                            showFormSession = false
                                            showFormRegister = false
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cerrar sesión")
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}


