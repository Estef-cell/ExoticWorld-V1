package com.example.exoticworld.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _currentUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)
    val currentUser = _currentUser

    // Registro
    fun registerUser(email: String, password: String){
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email,password).await()
                Log.d(javaClass.simpleName, "Usuario Registrado con exito")
            } catch (e : Exception) {
                Log.d(javaClass.simpleName, "Error en Registro: ${e.message}")
            }
        }
    }

    // Login/Inicio de sesion
     fun loginUser(email: String, password: String){
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email,password).await()
                Log.d(javaClass.simpleName, "Usuario Logeado con exito")
            } catch (e : Exception) {
                Log.d(javaClass.simpleName, "Error en login: ${e.message}")
            }
        }
    }

    //Cerrar sesion
    fun signOut(){
        try {
            auth.signOut()
        } catch (e : Exception) {
            Log.d(javaClass.simpleName, "${e.message}")
        }
    }

    //Comprobar estado del usuario
    fun listenAuthState(){
        auth.addAuthStateListener {
            this._currentUser.value = it.currentUser
        }
    }
}