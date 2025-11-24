package com.example.exoticworld.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoticworld.data.model.Animalitos
import com.example.exoticworld.data.model.Productos
import com.example.exoticworld.data.model.CartItem
import com.example.exoticworld.data.repository.AnimalitosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repoAnim: AnimalitosRepository = AnimalitosRepository()
) : ViewModel() {

    // Categorías
    private val _animalitos = MutableStateFlow<List<Animalitos>>(emptyList())
    val animalitos: StateFlow<List<Animalitos>> = _animalitos.asStateFlow()

    // Productos de ejemplo (puedes reemplazar por tu repo real)
    private val _productos = MutableStateFlow(
        listOf(
            Productos(1, "Alimento Tortuga 250g", "Pellets balanceados", 6990, 1),
            Productos(2, "Filtro para Tortuguera", "Filtro interno 400L/h", 25990, 1),
            Productos(3, "Alimento Loro 1kg", "Mezcla premium", 10990, 2),
            Productos(4, "Jaula mediana", "60×40×70 cm", 69990, 2),
            Productos(5, "Pecera 30L", "Vidrio 5mm", 45990, 3),
            Productos(6, "Comida Tropical 200g", "Escamas", 5990, 3),
            Productos(7, "Terrario 20×20×20", "Con ventilación", 34990, 4),
            Productos(8, "Pinzas alimentación", "Acero inoxidable", 4990, 4),
            Productos(9, "Juguete de cuerda", "Algodón trenzado", 5990, 5),
            Productos(10, "Snacks 200g", "Mix frutas", 3990, 5)
        )
    )
    val productos: StateFlow<List<Productos>> = _productos.asStateFlow()

    // Carrito
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    init {
        viewModelScope.launch { _animalitos.value = repoAnim.getAll() }
    }

    fun getAnimalito(id: Int): Animalitos? = repoAnim.getById(id)
    fun productosPorCategoria(categoriaId: Int): List<Productos> =
        _productos.value.filter { it.categoriaId == categoriaId }

    fun addToCart(p: Productos) {
        val current = _cart.value.toMutableList()
        val idx = current.indexOfFirst { it.producto.id == p.id }
        if (idx >= 0) current[idx] = current[idx].copy(quantity = current[idx].quantity + 1)
        else current += CartItem(producto = p, quantity = 1)
        _cart.value = current
    }

    fun decFromCart(productId: Int) {
        val current = _cart.value.toMutableList()
        val idx = current.indexOfFirst { it.producto.id == productId }
        if (idx >= 0) {
            val q = current[idx].quantity
            if (q <= 1) current.removeAt(idx) else current[idx] = current[idx].copy(quantity = q - 1)
            _cart.value = current
        }
    }

    fun removeFromCart(productId: Int) {
        _cart.value = _cart.value.filterNot { it.producto.id == productId }
    }

    fun clearCart() { _cart.value = emptyList() }

    fun total(): Int = _cart.value.sumOf { it.subtotal }
}
