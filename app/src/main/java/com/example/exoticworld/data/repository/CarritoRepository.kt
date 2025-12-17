package com.example.exoticworld.data.repository

import com.example.exoticworld.data.model.CarritoItemModel
import com.example.exoticworld.data.model.CarritoModel
import com.example.exoticworld.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio del carrito
 * Aca hago las llamadas HTTP al backend y manejo los errores
 * Uso Result<> para retornar exito o error
 */
class CarritoRepository(private val apiService: ApiService) {

    // Obtengo el carrito de un usuario
    suspend fun getCarritoPorUsuario(usuarioId: String): Result<CarritoModel> = withContext(Dispatchers.IO) {
        try {
            // Llamo a la API y obtengo la respuesta
            val response = apiService.getCarritoPorUsuario(usuarioId)
            // Verifico si fue exitoso (codigo HTTP 200-299) y si trajo datos
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)  // Retorno exito con los datos
            } else {
                Result.failure(Exception("Error al obtener carrito: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)  // Si fallo la conexion o algo, retorno el error
        }
    }

    // Agrego un producto al carrito
    suspend fun agregarProductoAlCarrito(
        usuarioId: String,
        productoId: Int,
        cantidad: Int = 1
    ): Result<CarritoItemModel> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.agregarProductoAlCarrito(usuarioId, productoId, cantidad)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al agregar producto: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Le bajo 1 a la cantidad de un producto
    // Si llega a 0, el backend lo elimina y retorna null
    suspend fun decrementarProductoEnCarrito(
        usuarioId: String,
        productoId: Int
    ): Result<CarritoItemModel?> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.decrementarProductoEnCarrito(usuarioId, productoId)
            if (response.isSuccessful) {
                // Aca no verifico si body es null porque a veces puede ser null y esta bien
                // (cuando la cantidad llega a 0 el backend elimina el item y retorna null)
                Result.success(response.body())
            } else {
                Result.failure(Exception("Error al decrementar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarCantidadProducto(
        usuarioId: String,
        productoId: Int,
        cantidad: Int
    ): Result<CarritoItemModel> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.actualizarCantidadProducto(usuarioId, productoId, cantidad)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar cantidad: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getItemsDelCarrito(usuarioId: String): Result<List<CarritoItemModel>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getItemsDelCarrito(usuarioId)
            if (response.isSuccessful) {
                // Si el body es null o vacío, devolver lista vacía
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener items: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTotalCarrito(usuarioId: String): Result<Double> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTotalCarrito(usuarioId)
            if (response.isSuccessful) {
                // Si el body es null, devolver 0.0
                Result.success(response.body()?.total ?: 0.0)
            } else {
                Result.failure(Exception("Error al obtener total: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun vaciarCarrito(usuarioId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.vaciarCarrito(usuarioId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al vaciar carrito: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarItemDelCarrito(
        usuarioId: String,
        productoId: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.eliminarItemDelCarrito(usuarioId, productoId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar item: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
