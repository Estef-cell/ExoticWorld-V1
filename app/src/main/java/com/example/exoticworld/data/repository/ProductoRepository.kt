package com.example.exoticworld.data.repository

import com.example.exoticworld.data.model.Productos
import com.example.exoticworld.data.remote.ApiClient

class ProductosRepository {
    private val api = ApiClient.service

    suspend fun getProductos(): List<Productos> = api.getProductos()
}