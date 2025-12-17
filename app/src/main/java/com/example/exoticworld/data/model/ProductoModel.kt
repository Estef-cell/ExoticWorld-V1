package com.example.exoticworld.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de un producto
 * Esto representa como llega un producto desde el backend en formato JSON
 * @SerializedName se usa para mapear los nombres del JSON con mis variables en Kotlin
 */
data class ProductoModel(
    @SerializedName("producto_id")       // En el JSON viene como "producto_id"
    val productoId: Int,                  // Pero yo lo uso como productoId en la app

    @SerializedName("nombreProducto")
    val nombreProducto: String,           // Nombre del producto (ej: "Tortuga")

    @SerializedName("descripcionProducto")
    val descripcionProducto: String,      // Descripcion del producto

    @SerializedName("precioProducto")
    val precioProducto: Double            // Precio en pesos chilenos
)
