# Print3D Store - Aplicación Android Nativa

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg)](https://firebase.google.com)
[![Material Design 3](https://img.shields.io/badge/UI-Material%20Design%203-blue.svg)](https://m3.material.io)

## 📱 Descripción

**Print3D Store** es una plataforma de comercio electrónico especializada en productos personalizados de manufactura aditiva 3D, desarrollada como aplicación Android nativa siguiendo los principios de Clean Architecture y MVVM.

Desarrollado por: **Yerson Alarcón**  
Docente: **Flavio Alexander Navarro Carmona**  
Institución: **Fundación Universitaria Compensar**  
Año: **2026**

## 🎯 Características Principales

### Roles del Sistema

#### 👤 Comprador (Buyer)
- Exploración de catálogo de productos 3D
- Personalización de artículos (material, color, relleno, escala)
- Carrito de compras con cálculo de costos
- Integración con geolocalización (GPS) para entregas
- Pasarela de pagos Wompi (PSE, Tarjetas, Nequi)
- Historial de pedidos

#### 🏪 Vendedor (Seller)
- Dashboard con métricas de ventas
- CRUD completo de productos
- Captura de fotos con CameraX
- Gestión de inventario
- Actualización de estados de pedidos
- Panel de órdenes pendientes

#### ⚙️ Administrador (Admin)
- Panel de control con métricas globales
- Gestión de usuarios del sistema
- Moderación y aprobación de productos
- Reportes de actividad
- Asignación de roles

### Características Técnicas

- ✅ **Autenticación biométrica** (huella dactilar)
- ✅ **Firebase Authentication** (Email/Password)
- ✅ **Cloud Firestore** (base de datos NoSQL en tiempo real)
- ✅ **Firebase Storage** (almacenamiento de imágenes)
- ✅ **CameraX** (captura de fotos optimizada)
- ✅ **Fused Location Provider** (geolocalización precisa)
- ✅ **Google Maps Integration** (selección de punto de entrega)
- ✅ **Wompi Payment Gateway** (pasarela de pagos colombiana)
- ✅ **Material Design 3** (diseño moderno y consistente)
- ✅ **Offline Support** (caché con Firestore)

## 🏗️ Arquitectura

El proyecto implementa **Clean Architecture** combinada con el patrón **MVVM** (Model-View-ViewModel) y **Repository Pattern**, siguiendo las recomendaciones oficiales de Google para desarrollo Android.

```
app/
├── data/              # Capa de datos (Repositories, Firebase)
├── domain/            # Capa de dominio (Models, UseCases)
├── presentation/      # Capa de presentación (Activities, Fragments, ViewModels)
│   ├── auth/         # Autenticación
│   ├── buyer/        # Pantallas del comprador
│   ├── seller/       # Pantallas del vendedor
│   └── admin/        # Pantallas del administrador
└── utils/             # Utilidades y helpers
```

## 🛠️ Stack Tecnológico

### Lenguaje y Framework
- **Kotlin** 1.9.20
- **Android SDK** 26+ (Android 8.0 Oreo en adelante)
- **Target SDK** 34

### Bibliotecas Principales
- **Jetpack Libraries**
  - Navigation Component
  - ViewModel & LiveData
  - ViewBinding
  - DataBinding
  
- **Firebase**
  - Firebase Auth
  - Cloud Firestore
  - Firebase Storage
  - Firebase Analytics

- **Interfaz de Usuario**
  - Material Design 3 Components
  - ConstraintLayout
  - RecyclerView
  - CardView

- **Hardware & Servicios**
  - CameraX 1.3.1
  - Biometric 1.1.0
  - Fused Location Provider 21.0.1
  - Google Maps 18.2.0

- **Networking**
  - Retrofit 2.9.0
  - OkHttp 4.12.0
  - Gson Converter

- **Imágenes**
  - Coil 2.5.0 (carga y caché de imágenes)

- **Seguridad**
  - EncryptedSharedPreferences
  - ProGuard/R8 (obfuscación)

## 📋 Requisitos Previos

- Android Studio Hedgehog | 2023.2.1 o superior
- JDK 17
- Android SDK con API Level 26+
- Cuenta de Firebase (para Backend)
- Google Maps API Key (para geolocalización)
- Cuenta Wompi para pagos (opcional para desarrollo)

## 🚀 Configuración del Proyecto

### 1. Abrir en Android Studio

1. Abrir Android Studio
2. File → Open
3. Seleccionar la carpeta `Print3DStore`
4. Esperar sincronización de Gradle

### 2. Configurar Firebase

1. Crear un proyecto en [Firebase Console](https://console.firebase.google.com)
2. Agregar una aplicación Android
3. Descargar el archivo `google-services.json`
4. Colocarlo en `app/google-services.json`
5. Habilitar los siguientes servicios:
   - Authentication (Email/Password)
   - Cloud Firestore
   - Storage

### 3. Configurar Google Maps

1. Obtener una API Key de [Google Cloud Console](https://console.cloud.google.com)
2. Crear archivo `local.properties` en la raíz del proyecto
3. Agregar la key:
   ```properties
   sdk.dir=/ruta/al/Android/Sdk
   MAPS_API_KEY=TU_API_KEY_AQUI
   ```

### 4. Ejecutar la aplicación

Desde Android Studio:
1. Conectar dispositivo Android o iniciar emulador
2. Click en `Run > Run 'app'`
3. Seleccionar dispositivo destino

## 📦 Generación del APK

### APK Debug
```bash
./gradlew assembleDebug
```
El APK se generará en: `app/build/outputs/apk/debug/`

### APK Release (firmado)
```bash
./gradlew assembleRelease
```
El APK se generará en: `app/build/outputs/apk/release/`

## 🔐 Seguridad

La aplicación implementa las siguientes medidas de seguridad:

- **Cifrado TLS 1.3** para todas las comunicaciones HTTP
- **EncryptedSharedPreferences** para datos sensibles locales
- **AndroidKeyStore** para almacenamiento de llaves biométricas
- **Firebase Security Rules** para control de acceso por rol (RBAC)
- **ProGuard/R8** para obfuscación del código
- **No almacenamiento de datos de tarjetas** en el dispositivo
- **Tokenización Wompi** para procesamiento de pagos

## ⚖️ Normatividad Legal Colombiana

La aplicación cumple con la siguiente normativa:

- **Ley 527 de 1999** - Comercio Electrónico
- **Ley 1480 de 2011** - Estatuto del Consumidor (Derecho de Retracto)
- **Ley 1581 de 2012** - Protección de Datos Personales (Habeas Data)
- **Decreto 1377 de 2013 / 1074 de 2015** - Tratamiento de datos
- **Decreto 587 de 2016** - Reversión de Pagos
- **Ley 2439 de 2024** - Transparencia en Precios

## 🧪 Pruebas

### Pruebas Unitarias
```bash
./gradlew test
```

### Pruebas de Integración
```bash
./gradlew connectedAndroidTest
```

## 📸 Screenshots

*Las capturas de pantalla estarán disponibles en la carpeta `/screenshots`*

## 📄 Licencia

Este proyecto es desarrollado como parte del programa académico de Ingeniería de Software de la Fundación Universitaria Compensar.

## 👨‍💻 Autor

**Yerson Alarcón**  
Estudiante de Ingeniería de Software  
Fundación Universitaria Compensar

## 🙏 Agradecimientos

- Docente: Flavio Alexander Navarro Carmona
- Fundación Universitaria Compensar
- Comunidad Android de Google

---

**Última actualización:** Noviembre 2026
