package com.example.exoticworld.navegation

object Routes {
    const val HOME = "home"
    const val PROFILE = "profiles"
    const val CART = "cart"
    const val SETTINGS = "settings"

    // Nueva ruta de detalle/categoría
    const val CATEGORY = "category/{id}"
    fun category(id: Int) = "category/$id"
}
