package com.example.exoticworld.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo del carrito completo
 */
data class CarritoModel(
    @SerializedName("carrito_id")
    val carritoId: Int,

    @SerializedName("usuarioId")
    val usuarioId: String
)
