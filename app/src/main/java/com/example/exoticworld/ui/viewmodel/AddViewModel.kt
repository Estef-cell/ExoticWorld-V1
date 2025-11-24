package com.example.exoticworld.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.exoticworld.data.model.FormAddState
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.sql.Time
import kotlin.concurrent.timerTask

class AddViewModel : ViewModel() {
    // StateFlow privado para mutar estado

    private val _formState = MutableStateFlow(FormAddState())

    // StateFlow público expuesto a la UI (solo lectura)
    val formState: StateFlow<FormAddState> = _formState

    // manejo de cambios en "correoUsuario"
    fun onCorreoChange(value: String) {
        _formState.update { state ->
            val error = when {
                value.isBlank() -> "El correo no puede estar vacío"
                !value.contains("@") -> "Debe llevar @"
                !isValidEmail(value) -> "El correo no admite el formato"
                else -> null
            }
            state.copy(
                correoUsuario = value,
                correoUsuarioError = error,
                isValid = validateForm(state.copy(correoUsuario = value,correoUsuarioError = error)),
                isValidLogin = validateLogin(state.copy(correoUsuario = value,correoUsuarioError = error))
            )
        }
    }

    // manejo de cambios en "contrasena"
    fun onContrasenaChange(value: String){
        _formState.update { state ->
            val error = when {
                value.isBlank() -> "La contraseña no puede estar vacía"
                value.length < 6 -> "La contraseña debe contener al menos 6 caracteres"
                else -> null
            }
            state.copy(
                contrasena = value,
                contrasenaError = error,
                isValid = validateForm(state.copy(contrasena = value, contrasenaError = error)),
                isValidLogin = validateLogin(state.copy(contrasena = value, contrasenaError = error))
            )

        }
    }


    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return email.matches(emailRegex)
    }
    fun onConfirmarContrasenaChange(value: String){
        _formState.update { state ->
            val error = when {
                value.isBlank() -> "Debe confirmar la contraseña"
                value!= state.contrasena -> "Las contraseñas no coinciden"
                else -> null
            }
            state.copy(
                confirmarContrasena  = value,
                confirmarContrasenaError = error,
                isValid = validateForm(state.copy(confirmarContrasena = value, confirmarContrasenaError = error))
            )

        }
    }


    //validacion del formulario
    private fun validateForm(state: FormAddState): Boolean {
        return  state.correoUsuarioError == null &&
                state.contrasenaError == null &&
                state.confirmarContrasenaError == null &&
                //state.nombre.isNotBlank() &&
                state.correoUsuario.isNotBlank() &&
                state.contrasena.isNotBlank() &&
                state.confirmarContrasena.isNotBlank()
    }

    private fun validateLogin(state: FormAddState): Boolean {
        return  state.correoUsuarioError == null &&
                state.contrasenaError == null &&
                //state.nombre.isNotBlank() &&
                state.correoUsuario.isNotBlank() &&
                state.contrasena.isNotBlank()
    }

    fun onLoginSubmit() {
        _formState.value = FormAddState() // vacia el formulario

    }


}
