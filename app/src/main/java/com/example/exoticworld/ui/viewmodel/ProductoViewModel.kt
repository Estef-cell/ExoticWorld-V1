package com.example.exoticworld.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.exoticworld.data.local.UserPreferences
import com.example.exoticworld.data.model.CarritoItemModel
import com.example.exoticworld.data.model.ProductoModel
import com.example.exoticworld.data.model.UiState
import com.example.exoticworld.data.remote.ApiClient
import com.example.exoticworld.data.repository.CarritoRepository
import com.example.exoticworld.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Este es el ViewModel, basicamente la "logica" de la app
 * Aca manejo todo lo de productos y carrito
 * Uso StateFlow para que la interfaz se actualize sola cuando cambian los datos
 */
class ProductoViewModel(application: Application) : AndroidViewModel(application) {

    // Creo los repositorios que se conectan con la API
    private val productoRepository = ProductoRepository(ApiClient.service)
    private val carritoRepository = CarritoRepository(ApiClient.service)

    // Esto me sirve para guardar el ID del usuario en el celular
    private val userPreferences = UserPreferences(application)

    // Estados de los productos (Idle = sin hacer nada, Loading = cargando, Success = exito, Error = fallo)
    // Uso el _ para que solo yo pueda modificarlo desde el ViewModel
    private val _productosState = MutableStateFlow<UiState<List<ProductoModel>>>(UiState.Idle)
    val productosState: StateFlow<UiState<List<ProductoModel>>> = _productosState.asStateFlow()

    // Estado de la busqueda de productos (cuando uso el buscador)
    private val _searchState = MutableStateFlow<UiState<List<ProductoModel>>>(UiState.Idle)
    val searchState: StateFlow<UiState<List<ProductoModel>>> = _searchState.asStateFlow()

    // Estado del carrito (lista de items que agregue)
    private val _carritoState = MutableStateFlow<UiState<List<CarritoItemModel>>>(UiState.Idle)
    val carritoState: StateFlow<UiState<List<CarritoItemModel>>> = _carritoState.asStateFlow()

    // El total a pagar del carrito (suma de todo)
    private val _totalCarrito = MutableStateFlow(0.0)
    val totalCarrito: StateFlow<Double> = _totalCarrito.asStateFlow()

    // Mensajes de error para mostrar al usuario
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Guardo el ID del usuario que esta usando la app
    private val _usuarioId = MutableStateFlow<String?>(null)
    val usuarioId: StateFlow<String?> = _usuarioId.asStateFlow()

    // init = este codigo se ejecuta cuando se crea el ViewModel
    init {
        viewModelScope.launch {
            // Cargo el ID del usuario que esta guardado en el celular
            _usuarioId.value = userPreferences.usuarioId.first()
            // Cargo los productos y el carrito automaticamente
            cargarProductos()
            cargarCarrito()
        }
    }

    // ============ PRODUCTOS ============

    // Esta funcion trae todos los productos del backend
    fun cargarProductos() {
        viewModelScope.launch {  // launch = ejecuta esto en segundo plano
            _productosState.value = UiState.Loading  // Muestro que esta cargando
            try {
                // Llamo al repositorio que se conecta con la API
                val result = productoRepository.getProductos()
                result.onSuccess { productos ->
                    // Si funciono, guardo los productos en el estado
                    _productosState.value = UiState.Success(productos)
                }.onFailure { error ->
                    // Si fallo, muestro el error
                    _productosState.value = UiState.Error(error.message ?: "Error al cargar productos")
                    _errorMessage.value = "Error: ${error.message}"
                }
            } catch (e: Exception) {
                // Por si algo sale muy mal
                _productosState.value = UiState.Error(e.message ?: "Error desconocido")
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun buscarProductosPorNombre(nombre: String) {
        if (nombre.isBlank()) {
            _searchState.value = UiState.Idle
            return
        }

        viewModelScope.launch {
            _searchState.value = UiState.Loading
            try {
                val result = productoRepository.buscarProductosPorNombre(nombre)
                result.onSuccess { productos ->
                    _searchState.value = UiState.Success(productos)
                }.onFailure { error ->
                    _searchState.value = UiState.Error(error.message ?: "Error en búsqueda")
                    _errorMessage.value = "Error de búsqueda: ${error.message}"
                }
            } catch (e: Exception) {
                _searchState.value = UiState.Error(e.message ?: "Error desconocido")
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun limpiarBusqueda() {
        _searchState.value = UiState.Idle
    }

    // ============ CARRITO ============
    fun cargarCarrito() {
        val userId = _usuarioId.value ?: return

        viewModelScope.launch {
            _carritoState.value = UiState.Loading
            try {
                val result = carritoRepository.getItemsDelCarrito(userId)
                result.onSuccess { items ->
                    _carritoState.value = UiState.Success(items)
                    calcularTotal(userId)
                }.onFailure { error ->
                    _carritoState.value = UiState.Error(error.message ?: "Error al cargar carrito")
                    _errorMessage.value = "Error carrito: ${error.message}"
                }
            } catch (e: Exception) {
                _carritoState.value = UiState.Error(e.message ?: "Error desconocido")
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun calcularTotal(userId: String) {
        viewModelScope.launch {
            try {
                // Intentar obtener el total desde la API
                val result = carritoRepository.getTotalCarrito(userId)
                result.onSuccess { total ->
                    _totalCarrito.value = total
                }.onFailure {
                    // Si falla la API, calcular localmente
                    calcularTotalLocal()
                }
            } catch (e: Exception) {
                // Si hay excepción, calcular localmente
                calcularTotalLocal()
            }
        }
    }

    private fun calcularTotalLocal() {
        val currentState = _carritoState.value
        if (currentState is UiState.Success) {
            val items = currentState.data
            val total = items.sumOf { it.subtotal }
            _totalCarrito.value = total
        } else {
            _totalCarrito.value = 0.0
        }
    }

    fun agregarProductoAlCarrito(productoId: Int, cantidad: Int = 1) {
        val userId = _usuarioId.value ?: return

        viewModelScope.launch {
            try {
                val result = carritoRepository.agregarProductoAlCarrito(userId, productoId, cantidad)
                result.onSuccess {
                    cargarCarrito() // Recarga el carrito después de agregar
                }.onFailure { error ->
                    _errorMessage.value = "Error al agregar: ${error.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun decrementarProductoEnCarrito(productoId: Int) {
        val userId = _usuarioId.value ?: return

        viewModelScope.launch {
            try {
                val result = carritoRepository.decrementarProductoEnCarrito(userId, productoId)
                result.onSuccess {
                    cargarCarrito()
                }.onFailure { error ->
                    _errorMessage.value = "Error al decrementar: ${error.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun eliminarProductoDelCarrito(productoId: Int) {
        val userId = _usuarioId.value ?: return

        viewModelScope.launch {
            try {
                val result = carritoRepository.eliminarItemDelCarrito(userId, productoId)
                result.onSuccess {
                    cargarCarrito()
                }.onFailure { error ->
                    _errorMessage.value = "Error al eliminar: ${error.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun vaciarCarrito() {
        val userId = _usuarioId.value ?: return

        viewModelScope.launch {
            try {
                val result = carritoRepository.vaciarCarrito(userId)
                result.onSuccess {
                    cargarCarrito()
                }.onFailure { error ->
                    _errorMessage.value = "Error al vaciar carrito: ${error.message}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun limpiarErrorMessage() {
        _errorMessage.value = null
    }
}
