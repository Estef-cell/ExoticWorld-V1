package com.example.exoticworld.repository

import com.example.exoticworld.data.model.ProductoModel
import com.example.exoticworld.data.remote.ApiService
import com.example.exoticworld.data.repository.ProductoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

/**
 * TESTS UNITARIOS del ProductoRepository
 *
 * Aca pruebo que mi repositorio funcione bien sin tener que conectarme al backend real
 * Uso Mockito para "simular" las respuestas de la API
 *
 * Por que hago tests?
 * - Para verificar que mi codigo funciona bien
 * - Para encontrar bugs antes de que los vea el usuario
 * - Para que el profe vea que se hacer pruebas unitarias
 */
@ExperimentalCoroutinesApi
class ProductoRepositoryTest {

    // @Mock = esto es una simulacion del ApiService (no es el real)
    @Mock
    private lateinit var mockApiService: ApiService

    // El repositorio que voy a probar
    private lateinit var repository: ProductoRepository

    // @Before = este codigo se ejecuta antes de cada test
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)  // Inicializo los mocks
        repository = ProductoRepository(mockApiService)  // Creo el repositorio con el mock
    }

    /**
     * TEST 1: Probar que se obtienen los productos correctamente
     *
     * Que hace este test?
     * - Simulo que la API me retorna 2 productos
     * - Llamo a getProductos() del repository
     * - Verifico que me retorne esos mismos 2 productos
     *
     * Por que es importante?
     * - Me aseguro que el repositorio funciona bien cuando todo sale bien
     */
    @Test
    fun `getProductos retorna lista exitosamente cuando API responde correctamente`() = runTest {
        // 1. PREPARAR - Creo productos de mentira para la prueba
        val productosEsperados = listOf(
            ProductoModel(1, "Producto Test 1", "Descripción 1", 100.0),
            ProductoModel(2, "Producto Test 2", "Descripción 2", 200.0)
        )

        // Le digo al mock: "cuando te llamen a getProductos(), retorna estos productos"
        whenever(mockApiService.getProductos()).thenReturn(productosEsperados)

        // 2. EJECUTAR - Llamo al metodo que quiero probar
        val result = repository.getProductos()

        // 3. VERIFICAR - Chequeo que todo funciono bien
        assertTrue("El resultado debe ser exitoso", result.isSuccess)
        assertEquals("Debe retornar 2 productos", 2, result.getOrNull()?.size)
        assertEquals("Primer producto debe ser Producto Test 1",
            "Producto Test 1",
            result.getOrNull()?.get(0)?.nombreProducto
        )

        // Verifico que efectivamente se llamo a la API (1 vez)
        verify(mockApiService, times(1)).getProductos()
    }

    /**
     * TEST 2: Probar que maneja bien los errores
     *
     * Que hace este test?
     * - Simulo que la API tira un error (ej: sin internet)
     * - Verifico que el repositorio no explote, sino que retorne un error controlado
     *
     * Por que es importante?
     * - Si hay un error de conexion, la app no debe crashear
     * - Debe mostrar un mensaje de error al usuario
     */
    @Test
    fun `getProductos retorna error cuando API falla`() = runTest {
        // 1. PREPARAR - Simulo un error de red
        val errorEsperado = RuntimeException("Error de conexión")
        // Le digo al mock: "cuando te llamen, tira este error"
        whenever(mockApiService.getProductos()).thenThrow(errorEsperado)

        // 2. EJECUTAR
        val result = repository.getProductos()

        // 3. VERIFICAR - Chequeo que retorno un error (no exploto)
        assertTrue("El resultado debe ser un error", result.isFailure)
        assertNotNull("Debe contener una excepción", result.exceptionOrNull())
        assertEquals("El mensaje de error debe coincidir",
            "Error de conexión",
            result.exceptionOrNull()?.message
        )
    }

    /**
     * TEST 3: Buscar producto por nombre
     *
     * QUÉ VALIDA:
     * - La búsqueda funciona correctamente
     * - Se pasa el parámetro correcto al API
     *
     * POR QUÉ ES IMPORTANTE:
     * - La búsqueda es una funcionalidad clave del proyecto
     */
    @Test
    fun `buscarProductosPorNombre retorna productos filtrados`() = runTest {
        // ARRANGE
        val nombreBusqueda = "Tortuga"
        val productosEsperados = listOf(
            ProductoModel(1, "Alimento Tortuga", "Comida especial", 50.0)
        )
        whenever(mockApiService.buscarProductosPorNombre(nombreBusqueda))
            .thenReturn(productosEsperados)

        // ACT
        val result = repository.buscarProductosPorNombre(nombreBusqueda)

        // ASSERT
        assertTrue("Debe ser exitoso", result.isSuccess)
        assertEquals("Debe retornar 1 producto", 1, result.getOrNull()?.size)
        assertTrue("El nombre debe contener 'Tortuga'",
            result.getOrNull()?.get(0)?.nombreProducto?.contains("Tortuga") == true
        )

        // Verificar que se pasó el parámetro correcto
        verify(mockApiService, times(1)).buscarProductosPorNombre(nombreBusqueda)
    }

    /**
     * TEST 4: Obtener producto por ID
     *
     * QUÉ VALIDA:
     * - Se puede obtener un producto específico por su ID
     */
    @Test
    fun `getProductoById retorna producto correcto`() = runTest {
        // ARRANGE
        val productoId = 5
        val productoEsperado = ProductoModel(
            productoId,
            "Producto Específico",
            "Descripción detallada",
            150.0
        )
        whenever(mockApiService.getProductoById(productoId)).thenReturn(productoEsperado)

        // ACT
        val result = repository.getProductoById(productoId)

        // ASSERT
        assertTrue("Debe ser exitoso", result.isSuccess)
        assertEquals("El ID debe coincidir", productoId, result.getOrNull()?.productoId)
        assertEquals("El nombre debe coincidir",
            "Producto Específico",
            result.getOrNull()?.nombreProducto
        )
    }
}
