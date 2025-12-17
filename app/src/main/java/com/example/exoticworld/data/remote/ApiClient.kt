package com.example.exoticworld.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Configuracion de Retrofit para conectarme con el backend
 * Aca defino la URL base, timeouts y como manejar las respuestas
 */
object ApiClient {

    // URL del backend (deploy en Render)
    private const val BASE_URL = "https://exoticworld-backend.onrender.com/"

    // Interceptor para ver en Logcat las peticiones HTTP (util para debuguear)
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // Muestra el body completo
    }

    // Configuracion del cliente HTTP
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Espera 30s para conectar
        .readTimeout(60, TimeUnit.SECONDS)     // Espera 60s para leer la respuesta
        .addInterceptor(logging)               // Agrego el interceptor para ver logs
        .build()

    // Configuracion de Gson (convierte JSON a objetos Kotlin)
    // Lo configure para que no explote si recibe null del backend
    private val gson = GsonBuilder()
        .serializeNulls()  // Permite enviar y recibir nulls
        .setLenient()      // Es mas tolerante con JSON mal formado
        .create()

    // Aca creo el servicio de Retrofit que usa los endpoints de ApiService
    // lazy = se crea solo cuando lo necesito por primera vez
    val service: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)                                      // URL base
            .client(client)                                          // Cliente HTTP configurado
            .addConverterFactory(GsonConverterFactory.create(gson))  // Conversor JSON
            .build()
            .create(ApiService::class.java)                         // Creo la interfaz ApiService
    }
}


