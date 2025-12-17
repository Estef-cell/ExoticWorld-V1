package com.example.exoticworld.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo que representa un item dentro del carrito
 */
data class CarritoItemModel(
    @SerializedName("item_id")
    val itemId: Int,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("carrito")
    val carrito: CarritoModel,

    @SerializedName("producto")
    val producto: ProductoModel
) {
    val subtotal: Double get() = producto.precioProducto * cantidad
}
