package com.example.exoticworld.navegation

object Routes {
    const val HOME = "home"
    const val PROFILE = "profiles"
    const val CART = "cart"
    const val SETTINGS = "settings"

    // Ruta de detalle/categoría
    const val CATEGORY = "category/{id}"
    fun category(id: Int) = "category/$id"

    // Ruta de detalle de producto
    const val PRODUCT_DETAIL = "product/{id}"
    fun productDetail(id: Int) = "product/$id"
}
