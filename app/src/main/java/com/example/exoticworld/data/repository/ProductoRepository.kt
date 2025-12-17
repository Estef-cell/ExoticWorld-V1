package com.example.exoticworld.data.repository

import com.example.exoticworld.data.model.ProductoModel
import com.example.exoticworld.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository para operaciones de productos con manejo de errores
 */
class ProductoRepository(private val apiService: ApiService) {

    suspend fun getProductos(): Result<List<ProductoModel>> = withContext(Dispatchers.IO) {
        try {
            val productos = apiService.getProductos()
            Result.success(productos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductoById(id: Int): Result<ProductoModel> = withContext(Dispatchers.IO) {
        try {
            val producto = apiService.getProductoById(id)
            Result.success(producto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun buscarProductosPorNombre(nombre: String): Result<List<ProductoModel>> = withContext(Dispatchers.IO) {
        try {
            val productos = apiService.buscarProductosPorNombre(nombre)
            Result.success(productos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearProducto(producto: ProductoModel): Result<ProductoModel> = withContext(Dispatchers.IO) {
        try {
            val nuevoProducto = apiService.crearProducto(producto)
            Result.success(nuevoProducto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarProducto(id: Int, producto: ProductoModel): Result<ProductoModel> = withContext(Dispatchers.IO) {
        try {
            val productoActualizado = apiService.actualizarProducto(id, producto)
            Result.success(productoActualizado)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarProducto(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            apiService.eliminarProducto(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}