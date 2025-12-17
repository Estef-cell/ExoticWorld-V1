package com.example.exoticworld.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Gestión de preferencias locales usando DataStore
 * Almacena el ID del usuario para persistencia
 */
class UserPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")
        private val USER_ID_KEY = stringPreferencesKey("user_id")

        // Usuario por defecto si no hay login
        private const val DEFAULT_USER_ID = "usuario_demo_001"
    }

    val usuarioId: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY] ?: DEFAULT_USER_ID
        }

    suspend fun guardarUsuarioId(usuarioId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = usuarioId
        }
    }

    suspend fun limpiarDatos() {
        context.dataStore.edit { it.clear() }
    }
}
