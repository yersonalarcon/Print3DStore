# Instrucciones de Compilación y Ejecución

## Print3D Store - Aplicación Android Nativa

---

## 📋 Requisitos del Sistema

- **Android Studio**: Hedgehog | 2023.2.1 o superior
- **JDK**: 17
- **Android SDK**: API Level 26 (Android 8.0) o superior
- **Gradle**: 8.2.0 (incluido en el proyecto)

---

## 🔧 Instalación y Configuración

### Paso 1: Abrir el Proyecto

1. Abrir **Android Studio**
2. Seleccionar **File → Open**
3. Navegar a la carpeta del proyecto **Print3DStore**
4. Click en **OK**
5. Esperar a que Gradle sincronice las dependencias (puede tardar algunos minutos)

### Paso 2: Configurar Firebase (Opcional para demostración)

Si se desea probar la funcionalidad de autenticación:

1. Crear un proyecto en [Firebase Console](https://console.firebase.google.com)
2. Agregar una aplicación Android con package: `com.compensar.print3dstore`
3. Descargar el archivo `google-services.json`
4. Colocar en: `app/google-services.json`
5. Habilitar **Authentication** (Email/Password) en Firebase Console

> **Nota**: El proyecto compila y ejecuta sin Firebase, pero la autenticación no funcionará.

### Paso 3: Configurar Google Maps API (Opcional)

Para la funcionalidad de geolocalización:

1. Obtener API Key en [Google Cloud Console](https://console.cloud.google.com)
2. Crear archivo `local.properties` en la raíz del proyecto
3. Agregar:
   ```properties
   sdk.dir=/ruta/a/tu/Android/Sdk
   MAPS_API_KEY=tu_api_key_aqui
   ```

> **Nota**: Sin esta configuración, la selección de dirección no funcionará.

---

## ▶️ Ejecución de la Aplicación

### Opción 1: Emulador (Recomendado)

1. En Android Studio: **Tools → Device Manager**
2. Crear un dispositivo virtual:
   - **Device**: Pixel 6
   - **System Image**: Android 14 (API 34) o superior
   - Click **Finish**
3. Click en el botón **Run ▶️** (Shift + F10)
4. Seleccionar el emulador creado

### Opción 2: Dispositivo Físico

1. Habilitar **Opciones de desarrollador** en el dispositivo Android
2. Activar **Depuración USB**
3. Conectar el dispositivo por USB
4. Autorizar la conexión en el dispositivo
5. Click en **Run ▶️** en Android Studio
6. Seleccionar el dispositivo conectado

---

## 🏗️ Generación del APK

### APK de Depuración (Debug)

```bash
./gradlew assembleDebug
```

El APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### APK de Producción (Release)

```bash
./gradlew assembleRelease
```

El APK se generará en: `app/build/outputs/apk/release/app-release-unsigned.apk`

> **Nota**: Para firmar el APK de release, se requiere configurar un keystore en `build.gradle`.

---

## 🧪 Ejecución de Pruebas

### Pruebas Unitarias

```bash
./gradlew test
```

### Pruebas de Interfaz (Instrumentadas)

```bash
./gradlew connectedAndroidTest
```

> **Nota**: Las pruebas instrumentadas requieren un dispositivo o emulador conectado.

---

## 📱 Funcionalidades Principales Implementadas

### ✅ Pantallas Completas por Rol

#### Comprador (Buyer)
- Splash Screen
- Login y Registro
- Catálogo de productos con búsqueda
- Detalle de producto con personalización
- Carrito de compras
- Checkout con selección de dirección
- Perfil de usuario
- Historial de pedidos

#### Vendedor (Seller)
- Dashboard con métricas de ventas
- Gestión de inventario (lista de productos)
- Formulario para agregar nuevo producto
- Captura de fotos con CameraX
- Panel de pedidos pendientes
- Perfil

#### Administrador (Admin)
- Panel de control con métricas globales
- Gestión de usuarios
- Moderación de productos (aprobar/rechazar)
- Dashboard de estadísticas

### ✅ Características Técnicas

- **Autenticación**: Login y Registro con Firebase Auth
- **Autenticación Biométrica**: Soporte para huella dactilar
- **Material Design 3**: Diseño moderno y consistente
- **Colores por Rol**: Azul (Comprador), Morado (Vendedor), Rojo (Admin)
- **Bottom Navigation**: Navegación intuitiva por rol
- **RecyclerViews**: Listas optimizadas de productos y pedidos
- **ViewBinding**: Acceso seguro a vistas XML
- **Responsive Layouts**: ConstraintLayout para adaptabilidad

---

## 🎨 Estructura del Código

### Organización de Paquetes

```
com.compensar.print3dstore/
├── domain/
│   └── model/              # Modelos de datos (User, Product, Order, Cart)
└── presentation/
    ├── auth/               # LoginActivity, RegisterActivity
    ├── buyer/              # BuyerMainActivity
    ├── seller/             # SellerMainActivity
    ├── admin/              # AdminMainActivity
    └── splash/             # SplashActivity
```

### Layouts XML (17 archivos)

- **Autenticación**: `activity_login`, `activity_register`, `activity_splash`
- **Comprador**: `activity_buyer_main`, `fragment_buyer_catalog`, `fragment_product_detail`, `fragment_cart`, `fragment_buyer_profile`
- **Vendedor**: `activity_seller_main`, `fragment_seller_dashboard`, `fragment_seller_products`, `activity_add_product`
- **Admin**: `activity_admin_main`, `fragment_admin_dashboard`, `fragment_admin_users`
- **Items**: `item_product_card`, `item_pending_product`

---

## 🔐 Permisos Requeridos

La aplicación solicita los siguientes permisos en tiempo de ejecución:

- **CAMERA**: Para capturar fotos de productos (Vendedor)
- **ACCESS_FINE_LOCATION**: Para calcular costo de envío (Comprador)
- **USE_BIOMETRIC**: Para autenticación por huella digital

---

## 📊 Base de Datos

El proyecto está configurado para usar **Cloud Firestore** (NoSQL) con las siguientes colecciones:

- `users/` - Información de usuarios
- `products/` - Catálogo de productos 3D
- `orders/` - Pedidos realizados
- `carts/` - Carritos de compra activos

---

## ⚖️ Cumplimiento Legal

La aplicación implementa requisitos de la normatividad colombiana:

- **Ley 1581 de 2012**: Checkbox de aceptación de política de privacidad
- **Ley 1480 de 2011**: Información de derecho de retracto
- **Ley 2439 de 2024**: Desglose transparente de precios

---

## 🚨 Solución de Problemas Comunes

### Error: "SDK location not found"
**Solución**: Crear archivo `local.properties` con:
```properties
sdk.dir=/ruta/a/tu/Android/Sdk
```

### Error: "google-services.json not found"
**Solución**: El proyecto compila sin este archivo, pero la autenticación no funcionará. Agregar archivo vacío o configurar Firebase.

### Error de sincronización de Gradle
**Solución**: 
1. File → Invalidate Caches → Invalidate and Restart
2. O ejecutar: `./gradlew clean build --refresh-dependencies`

### Emulador lento
**Solución**: 
1. Usar emulador con aceleración de hardware (HAXM en Intel, Hyper-V en Windows)
2. Asignar al menos 2GB de RAM al AVD

---

## 📞 Información del Proyecto

**Estudiante**: Yerson Alarcón  
**Asignatura**: Desarrollo de Aplicaciones Móviles Nativas  
**Docente**: Flavio Alexander Navarro Carmona  
**Institución**: Fundación Universitaria Compensar  
**Año**: 2026

---

## 📝 Entregables

- ✅ Código fuente completo
- ✅ Layouts XML (17 archivos)
- ✅ Activities Kotlin funcionales
- ✅ README con documentación
- ✅ Configuración de Firebase y Maps
- ✅ APK generado (debug)

---

**Última actualización**: Noviembre 2026
