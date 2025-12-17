# 🌟 ExoticWorld - Aplicación E-Commerce Android

## 📱 Proyecto Android para EFT

Aplicación Android completa desarrollada con **Kotlin**, **Jetpack Compose**, **MVVM** y **Retrofit** que consume una API REST real.

---

## 🚀 INICIO RÁPIDO

### Para ejecutar el proyecto:
1. Abre el proyecto en **Android Studio**
2. **Sync Gradle** (File → Sync Project with Gradle Files)
3. **Run 'app'**

### Para preparar tu defensa:
Lee la documentación completa en la carpeta **[📁 Documentacion/](Documentacion/)**

---

## 📚 DOCUMENTACIÓN COMPLETA

Toda la documentación del proyecto está organizada en la carpeta **[📁 Documentacion/](Documentacion/)**

### ⚡ ACCESO RÁPIDO

- **[📑 INDICE.md](Documentacion/INDICE.md)** - Índice completo de toda la documentación
- **[⚡ GUIA_RAPIDA.md](Documentacion/GUIA_RAPIDA.md)** - Inicio en 5 minutos

### 🎯 ORDEN DE LECTURA RECOMENDADO:

1. **[📖 GUIA_DEFENSA_EFT.md](Documentacion/GUIA_DEFENSA_EFT.md)** ⭐⭐⭐⭐⭐
   - Guía completa para la defensa técnica
   - Explicación de toda la arquitectura
   - Guión para presentar
   - Demostraciones paso a paso
   - Preguntas frecuentes con respuestas

2. **[🏗️ ARQUITECTURA_VISUAL.md](Documentacion/ARQUITECTURA_VISUAL.md)** ⭐⭐⭐⭐⭐
   - Diagramas visuales del proyecto
   - Flujos de datos completos
   - Cómo explicar la arquitectura en 3 minutos

3. **[🔧 EJERCICIOS_EN_VIVO.md](Documentacion/EJERCICIOS_EN_VIVO.md)** ⭐⭐⭐⭐☆
   - 11 ejercicios para practicar
   - Modificaciones fáciles, intermedias y avanzadas
   - Código completo de cada ejercicio

4. **[📝 RESUMEN_CAMBIOS.md](Documentacion/RESUMEN_CAMBIOS.md)** ⭐⭐⭐⭐☆
   - Lista de todos los cambios realizados
   - Archivos nuevos y modificados
   - Cómo probar cada funcionalidad

5. **[📄 README_PROYECTO_FINAL.md](Documentacion/README_PROYECTO_FINAL.md)** ⭐⭐⭐☆☆
   - Información general del proyecto
   - Tecnologías utilizadas
   - FAQ

---

## ✅ CARACTERÍSTICAS IMPLEMENTADAS

### Funcionalidades
- ✅ Listado de productos desde API REST
- ✅ Búsqueda de productos por nombre (con debounce)
- ✅ Detalle completo de producto
- ✅ Carrito de compras sincronizado con backend
- ✅ Agregar/eliminar/modificar productos en carrito
- ✅ Cálculo automático de total
- ✅ Persistencia de usuario con DataStore

### Arquitectura Técnica
- ✅ **MVVM** (Model-View-ViewModel)
- ✅ **Repository Pattern**
- ✅ **StateFlow** para estados reactivos
- ✅ **Retrofit** para consumo de API
- ✅ **DataStore** para persistencia local
- ✅ **Coroutines** para operaciones asíncronas
- ✅ **Manejo robusto de errores**
- ✅ **Pruebas unitarias** con Mockito

---

## 🔗 API BACKEND

**Base URL**: `https://exoticworld-backend.onrender.com`

**Endpoints principales**:
- `GET /api/v1/productos` - Listar productos
- `GET /api/v1/productos/buscar/nombre?nombre=XXX` - Buscar
- `POST /api/v1/carrito/usuario/{id}/agregar` - Agregar al carrito
- `GET /api/v1/carrito/usuario/{id}/items` - Items del carrito
- `GET /api/v1/carrito/usuario/{id}/total` - Total del carrito

---

## 🧪 PRUEBAS UNITARIAS

Ejecutar tests:
```bash
./gradlew test
```

O desde Android Studio:
- Click derecho en `ProductoRepositoryTest.kt`
- Run 'ProductoRepositoryTest'

---

## 📂 ESTRUCTURA DEL PROYECTO

```
ExoticWorld/
├── app/src/main/java/com/example/exoticworld/
│   ├── data/
│   │   ├── model/          # Modelos de datos
│   │   ├── remote/         # Retrofit y API
│   │   ├── repository/     # Repositories
│   │   └── local/          # DataStore
│   ├── ui/
│   │   ├── viewmodel/      # ViewModels
│   │   ├── screens/        # Pantallas Compose
│   │   └── theme/          # Temas
│   └── navegation/         # Navegación
├── app/src/test/           # Pruebas unitarias
└── Documentacion/          # 📚 Toda la documentación aquí
```

---

## 🛠️ TECNOLOGÍAS

- **Kotlin** 1.9+
- **Jetpack Compose** - UI declarativa
- **Material3** - Componentes de diseño
- **ViewModel & StateFlow** - Gestión de estados
- **Retrofit** 2.9.0 - Cliente HTTP
- **DataStore** 1.0.0 - Persistencia local
- **Mockito** 5.3.1 - Testing
- **Coroutines** - Programación asíncrona

---

## 🎓 PREPARACIÓN PARA LA DEFENSA

### ⏱️ Plan de Estudio (4-5 horas)

1. **Lee la documentación** (2 horas)
   - [GUIA_DEFENSA_EFT.md](Documentacion/GUIA_DEFENSA_EFT.md)
   - [ARQUITECTURA_VISUAL.md](Documentacion/ARQUITECTURA_VISUAL.md)

2. **Ejecuta la app** (30 minutos)
   - Prueba todas las funcionalidades
   - Observa Logcat con las llamadas HTTP

3. **Ejecuta los tests** (15 minutos)
   - Run 'ProductoRepositoryTest'
   - Verifica que todos pasen

4. **Practica ejercicios** (1 hora)
   - Haz al menos 3 ejercicios de [EJERCICIOS_EN_VIVO.md](Documentacion/EJERCICIOS_EN_VIVO.md)

5. **Practica el guión** (1 hora)
   - Ensaya las demostraciones
   - Cronometra cada explicación

---

## ✅ CHECKLIST PRE-DEFENSA

- [ ] He leído la documentación completa
- [ ] Puedo explicar la arquitectura MVVM
- [ ] Sé qué es StateFlow y por qué se usa
- [ ] Entiendo el flujo de datos de la app
- [ ] He ejecutado la app y funciona
- [ ] He ejecutado los tests y pasan
- [ ] Conozco los archivos clave del proyecto
- [ ] Puedo hacer modificaciones en vivo
- [ ] Sé manejar errores de compilación

---

## 🎯 CONCEPTOS CLAVE PARA EXPLICAR

### **MVVM**
- **Model**: Datos (ProductoModel, CarritoItemModel)
- **View**: UI en Compose (NewHomeScreen, NewCartScreen)
- **ViewModel**: Lógica de negocio (ProductoViewModel)

### **StateFlow**
Flow reactivo que mantiene un valor actual. La UI se actualiza automáticamente cuando cambia.

### **Repository Pattern**
Abstrae el origen de datos. El ViewModel no sabe si los datos vienen de API, BD o caché.

### **Result<T>**
Tipo de Kotlin que encapsula `Result.success(data)` o `Result.failure(exception)`.

---

## 📞 SOPORTE

Si encuentras problemas:

1. **Verifica el backend**: https://exoticworld-backend.onrender.com/api/v1/productos
2. **Limpia y reconstruye**:
   - Build → Clean Project
   - Build → Rebuild Project
3. **Invalida caché**:
   - File → Invalidate Caches / Restart

---

## 🏆 RESULTADO FINAL

✅ **Proyecto completo y funcional**
✅ **Arquitectura profesional MVVM**
✅ **Consumo real de API REST**
✅ **Pruebas unitarias implementadas**
✅ **Documentación exhaustiva**
✅ **Listo para defender en la EFT**

---

## 👨‍💻 DESARROLLO

**Proyecto desarrollado para**: Evaluación Final Transversal (EFT)
**Curso**: Desarrollo de Aplicaciones Móviles Android
**Tecnologías**: Kotlin, Jetpack Compose, MVVM, Retrofit

---

**¡ÉXITO EN TU DEFENSA!** 🚀

**Última actualización**: Diciembre 2025
**Versión**: 2.0 (Con API integrada)
