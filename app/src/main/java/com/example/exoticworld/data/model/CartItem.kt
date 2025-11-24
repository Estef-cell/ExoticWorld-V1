package com.example.exoticworld.data.model

data class CartItem(
    val producto: Productos,
    val quantity: Int = 1
) {
    val subtotal: Int get() = producto.precio * quantity
}
