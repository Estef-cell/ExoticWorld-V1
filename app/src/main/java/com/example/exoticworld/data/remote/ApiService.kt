package com.example.exoticworld.data.remote

import com.example.exoticworld.data.model.Productos
import retrofit2.http.GET

interface ApiService {

    @GET("api/v1/productos")
    suspend fun getProductos(): List<Productos>
}
