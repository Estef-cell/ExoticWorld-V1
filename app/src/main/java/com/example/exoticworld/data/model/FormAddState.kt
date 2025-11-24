package com.example.exoticworld.data.model

// representa el estado del formulario
data class FormAddState (
    //val nombre : String = "",
    val correoUsuario : String = "",
    val contrasena : String = "",
    val confirmarContrasena : String = "",

    //val nombreError : String? = null,
    val correoUsuarioError : String? = null,
    val contrasenaError : String? = null,
    val confirmarContrasenaError : String? = null,

    val isValid : Boolean = false,
    val isValidLogin : Boolean = false
)

