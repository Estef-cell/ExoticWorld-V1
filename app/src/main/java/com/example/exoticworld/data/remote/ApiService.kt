package com.example.exoticworld.data.remote

import com.example.exoticworld.data.model.CarritoItemModel
import com.example.exoticworld.data.model.CarritoModel
import com.example.exoticworld.data.model.CarritoTotalResponse
import com.example.exoticworld.data.model.ProductoModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Aca defino todos los endpoints de la API que voy a usar
 * Retrofit usa esto para saber como hacer las peticiones HTTP
 */
interface ApiService {

    // ============ PRODUCTOS ============

    // Obtengo todos los productos del backend
    @GET("api/v1/productos")
    suspend fun getProductos(): List<ProductoModel>

    // Obtengo un producto especifico por su ID
    @GET("api/v1/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Int): ProductoModel

    // Busco productos por nombre (para el buscador de la app)
    @GET("api/v1/productos/buscar/nombre")
    suspend fun buscarProductosPorNombre(@Query("nombre") nombre: String): List<ProductoModel>

    // Creo un producto nuevo (metodo POST)
    @POST("api/v1/productos")
    suspend fun crearProducto(@Body producto: ProductoModel): ProductoModel

    // Actualizo un producto existente
    @PUT("api/v1/productos/{id}")
    suspend fun actualizarProducto(@Path("id") id: Int, @Body producto: ProductoModel): ProductoModel

    // Elimino un producto
    @DELETE("api/v1/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Int)

    // ============ CARRITO POR USUARIO ============

    // Obtengo el carrito completo de un usuario
    // Uso Response<> porque a veces el backend puede retornar null
    @GET("api/v1/carrito/usuario/{usuarioId}")
    suspend fun getCarritoPorUsuario(@Path("usuarioId") usuarioId: String): Response<CarritoModel>

    // Agrego un producto al carrito
    // Por defecto la cantidad es 1 si no se especifica
    @POST("api/v1/carrito/usuario/{usuarioId}/agregar")
    suspend fun agregarProductoAlCarrito(
        @Path("usuarioId") usuarioId: String,        // El ID del usuario
        @Query("productoId") productoId: Int,        // El ID del producto
        @Query("cantidad") cantidad: Int = 1         // Cantidad a agregar
    ): Response<CarritoItemModel>

    // Le bajo 1 a la cantidad del producto en el carrito
    // Si llega a 0, el backend lo elimina automaticamente
    @POST("api/v1/carrito/usuario/{usuarioId}/decrementar")
    suspend fun decrementarProductoEnCarrito(
        @Path("usuarioId") usuarioId: String,
        @Query("productoId") productoId: Int
    ): Response<CarritoItemModel>

    // Cambio la cantidad de un producto del carrito
    @PUT("api/v1/carrito/usuario/{usuarioId}/actualizar-cantidad")
    suspend fun actualizarCantidadProducto(
        @Path("usuarioId") usuarioId: String,
        @Query("productoId") productoId: Int,
        @Query("cantidad") cantidad: Int
    ): Response<CarritoItemModel>

    // Obtengo todos los items que hay en el carrito
    @GET("api/v1/carrito/usuario/{usuarioId}/items")
    suspend fun getItemsDelCarrito(@Path("usuarioId") usuarioId: String): Response<List<CarritoItemModel>>

    // Calculo el total a pagar del carrito
    @GET("api/v1/carrito/usuario/{usuarioId}/total")
    suspend fun getTotalCarrito(@Path("usuarioId") usuarioId: String): Response<CarritoTotalResponse>

    // Vacio el carrito completamente
    @DELETE("api/v1/carrito/usuario/{usuarioId}/vaciar")
    suspend fun vaciarCarrito(@Path("usuarioId") usuarioId: String): Response<Unit>

    // Elimino un item especifico del carrito
    @DELETE("api/v1/carrito/usuario/{usuarioId}/eliminar-item")
    suspend fun eliminarItemDelCarrito(
        @Path("usuarioId") usuarioId: String,
        @Query("productoId") productoId: Int
    ): Response<Unit>
}
