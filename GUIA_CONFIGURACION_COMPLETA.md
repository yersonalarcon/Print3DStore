# 🚀 Guía Completa de Configuración - Print3D Store

Esta guía te llevará paso a paso para configurar y ejecutar el proyecto completo con todas sus funcionalidades.

---

## 📋 **PASO 1: Configurar Firebase**

### 1.1 Crear Proyecto en Firebase

1. Ve a [Firebase Console](https://console.firebase.google.com/)
2. Haz clic en **"Agregar proyecto"** o **"Create a project"**
3. Nombre del proyecto: `Print3D-Store` (o el que prefieras)
4. **NO actives** Google Analytics por ahora (puedes activarlo después)
5. Haz clic en **"Crear proyecto"** y espera

### 1.2 Agregar Android App

1. En el panel de Firebase, haz clic en el icono de **Android** 
2. Registra la app con estos datos:
   - **Package name**: `com.compensar.print3dstore` ⚠️ **IMPORTANTE: copiar exactamente**
   - **App nickname**: `Print3D Store`
   - **Debug signing certificate SHA-1**: (opcional por ahora)
3. Haz clic en **"Registrar app"**

### 1.3 Descargar google-services.json

1. Firebase te mostrará un botón para **descargar google-services.json**
2. **Descarga** ese archivo
3. **COPIA** el archivo `google-services.json` a:
   ```
   Print3DStore/app/google-services.json
   ```
   ⚠️ **Debe estar dentro de la carpeta `app/`**, NO en la raíz del proyecto

### 1.4 Habilitar Firebase Authentication

1. En el menú de Firebase, ve a **"Authentication"** (Autenticación)
2. Haz clic en **"Get started"** o **"Comenzar"**
3. En la pestaña **"Sign-in method"** (Método de inicio de sesión):
4. Habilita **"Email/Password"**:
   - Activa el toggle
   - **NO** actives "Email link (passwordless sign-in)" por ahora
   - Haz clic en **"Save"**

### 1.5 Crear Cloud Firestore Database

1. En el menú de Firebase, ve a **"Firestore Database"**
2. Haz clic en **"Create database"** (Crear base de datos)
3. Selecciona **"Start in test mode"** (Modo de prueba):
   - Esto permitirá lecturas/escrituras sin reglas por 30 días
   - ⚠️ Para producción, deberás configurar reglas de seguridad
4. Selecciona la ubicación: **"us-central1"** (o la más cercana a Colombia)
5. Haz clic en **"Enable"**

### 1.6 Configurar Firebase Storage

1. En el menú de Firebase, ve a **"Storage"**
2. Haz clic en **"Get started"** o **"Comenzar"**
3. Acepta las reglas en **modo de prueba**
4. Selecciona la misma ubicación que usaste para Firestore
5. Haz clic en **"Done"**

---

## 📋 **PASO 2: Configurar Google Maps API (para geolocalización)**

### 2.1 Obtener API Key de Google Maps

1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Selecciona tu proyecto de Firebase (se creó automáticamente)
3. En el menú lateral, ve a **"APIs & Services"** > **"Credentials"**
4. Haz clic en **"+ CREATE CREDENTIALS"** > **"API key"**
5. **Copia** la API Key que se genera
6. (Opcional pero recomendado) Haz clic en **"Restrict Key"**:
   - En "Application restrictions", selecciona **"Android apps"**
   - Agrega tu package name: `com.compensar.print3dstore`

### 2.2 Habilitar APIs necesarias

1. En Google Cloud Console, ve a **"APIs & Services"** > **"Library"**
2. Busca y **habilita** estas APIs:
   - **Maps SDK for Android**
   - **Places API**
   - **Geolocation API**

### 2.3 Agregar la API Key al proyecto

1. Abre el archivo `local.properties` (está en la raíz del proyecto)
2. Agrega esta línea al final:
   ```properties
   MAPS_API_KEY=TU_API_KEY_AQUI
   ```
   Reemplaza `TU_API_KEY_AQUI` con la API Key que copiaste

---

## 📋 **PASO 3: Configurar Android Studio**

### 3.1 Verificar instalación de JDK

1. Abre Android Studio
2. Ve a **File** > **Project Structure**
3. En **SDK Location**, verifica que **Gradle JDK** sea **17** o superior
4. Si no es así:
   - Haz clic en el dropdown de "Gradle JDK"
   - Selecciona **"Download JDK..."**
   - Descarga **JDK 17**

### 3.2 Sync del Proyecto

1. Con el proyecto abierto, haz clic en **"Sync Project with Gradle Files"** (icono de elefante con flecha azul)
2. Espera a que termine (puede tardar varios minutos la primera vez)
3. Si aparecen errores:
   - Ve a **File** > **Invalidate Caches / Restart**
   - Selecciona **"Invalidate and Restart"**

### 3.3 Verificar google-services.json

1. En Android Studio, en la vista **Project**, asegúrate de ver:
   ```
   app/
     ├── google-services.json  ✅ Debe estar aquí
     ├── build.gradle
     └── src/
   ```

---

## 📋 **PASO 4: Preparar Dispositivo para Pruebas**

### Opción A: Dispositivo Físico (RECOMENDADO para el video)

1. **Habilitar Modo Desarrollador** en tu teléfono:
   - Ve a **Configuración** > **Acerca del teléfono**
   - Toca **7 veces** sobre **"Número de compilación"**
   - Aparecerá un mensaje: "Ahora eres desarrollador"

2. **Habilitar Depuración USB**:
   - Ve a **Configuración** > **Opciones de desarrollador**
   - Activa **"Depuración USB"**
   - Activa **"Instalar apps vía USB"**

3. **Conectar el dispositivo**:
   - Conecta tu teléfono a la computadora con un cable USB
   - En el teléfono aparecerá un mensaje "¿Permitir depuración USB?"
   - Marca **"Permitir siempre desde esta computadora"**
   - Toca **"Permitir"**

4. **Verificar conexión**:
   - En Android Studio, arriba a la derecha, deberías ver tu dispositivo en el dropdown
   - Si no aparece, ejecuta en terminal:
     ```bash
     adb devices
     ```

### Opción B: Emulador (solo para pruebas, NO para el video final)

1. En Android Studio, ve a **Tools** > **Device Manager**
2. Haz clic en **"Create Device"**
3. Selecciona **Pixel 6** o cualquier dispositivo moderno
4. Selecciona **API 34** (Android 14)
5. Haz clic en **"Finish"**

---

## 📋 **PASO 5: Compilar y Ejecutar**

### 5.1 Primera Compilación

1. En Android Studio, selecciona tu dispositivo en el dropdown superior
2. Haz clic en el botón **▶ Run** (Play verde)
3. La primera vez tardará varios minutos en compilar
4. Si aparece un error de Firebase:
   - Verifica que `google-services.json` esté en `app/`
   - Haz clic en **"Sync Project with Gradle Files"** nuevamente

### 5.2 Probar el Login

1. Cuando la app se instale, verás la pantalla de Login
2. Haz clic en **"¿No tienes cuenta? Regístrate"**
3. Registra un usuario de prueba:
   - Nombre completo: Tu Nombre
   - Email: `test@print3d.com`
   - Teléfono: `3001234567`
   - Contraseña: `123456`
   - Rol: **Comprador**
4. Haz clic en **"Registrarse"**
5. Si todo está bien, serás redirigido a la pantalla principal del Comprador

---

## 📋 **PASO 6: Crear Usuarios de Prueba para Todos los Roles**

Para probar todas las funcionalidades, necesitas 3 usuarios:

### Usuario 1: Comprador
```
Email: comprador@print3d.com
Contraseña: 123456
Rol: Comprador
```

### Usuario 2: Vendedor
```
Email: vendedor@print3d.com
Contraseña: 123456
Rol: Vendedor
```

### Usuario 3: Administrador
1. Primero, regístralo como Comprador
2. Luego, ve a Firebase Console > Firestore Database
3. Busca la colección `users`
4. Encuentra el documento del usuario admin
5. Edita el campo `role` y cámbialo a `ADMIN`
6. Guarda los cambios

---

## 📋 **PASO 7: Probar Funcionalidades**

### 7.1 Como Vendedor

1. Inicia sesión con el usuario vendedor
2. **Crear un producto**:
   - Toca el botón **"+"** flotante
   - Toma una foto del producto (o selecciona de galería)
   - Llena los datos:
     - Nombre: "Figura 3D de Pikachu"
     - Descripción: "Impresión 3D en PLA amarillo"
     - Precio: 25000
     - Stock: 10
     - Categoría: "Figuras"
   - Toca **"Guardar"**
   - El producto quedará en estado **PENDIENTE** hasta que un admin lo apruebe

### 7.2 Como Administrador

1. Cierra sesión y entra con el usuario admin
2. Ve a la pestaña **"Productos"**
3. Verás los productos pendientes
4. Toca en un producto y **"Aprobar"**
5. Ve a la pestaña **"Usuarios"**
6. Verás todos los usuarios registrados
7. Puedes **Activar/Desactivar** usuarios

### 7.3 Como Comprador

1. Cierra sesión y entra con el usuario comprador
2. Ve al **Catálogo**
3. Verás los productos aprobados
4. Toca en un producto para ver detalle
5. **Agregar al carrito**:
   - Selecciona cantidad
   - Toca **"Agregar al Carrito"**
6. Ve a la pestaña **"Carrito"**
7. Verás tus productos
8. Toca **"Realizar Pedido"**
9. Llena la dirección de entrega
10. Selecciona método de pago
11. Toca **"Confirmar Pedido"**

---

## 📋 **PASO 8: Verificar Geolocalización**

1. En la pantalla de crear pedido, debería haber un botón **"Usar mi ubicación"**
2. Tócalo
3. El sistema pedirá permisos de ubicación
4. Acepta los permisos
5. La dirección se llenará automáticamente con tus coordenadas

**Si no funciona:**
- Verifica que el dispositivo tenga GPS activado
- Verifica que hayas aceptado los permisos
- Prueba en un lugar con buena señal de GPS (exterior preferiblemente)

---

## 📋 **PASO 9: Crear el Manual de Usuario (para entregar)**

### 9.1 Estructura del Manual

Crea un documento PDF o Word con estas secciones:

1. **Portada**
   - Título: "Manual de Usuario - Print3D Store"
   - Tu nombre
   - Universidad Compensar
   - Fecha

2. **Introducción**
   - ¿Qué es Print3D Store?
   - Objetivo de la aplicación

3. **Requisitos del Sistema**
   - Android 8.0 o superior
   - Conexión a Internet
   - Cámara (para vendedores)
   - GPS (opcional)

4. **Guía por Rol - Comprador** (con capturas de pantalla)
   - Cómo registrarse
   - Cómo navegar el catálogo
   - Cómo buscar productos
   - Cómo ver detalle de producto
   - Cómo agregar al carrito
   - Cómo realizar un pedido
   - Cómo ver mis pedidos

5. **Guía por Rol - Vendedor** (con capturas de pantalla)
   - Cómo crear un producto
   - Cómo tomar foto del producto
   - Cómo ver mis productos
   - Cómo editar un producto
   - Cómo ver mis ventas
   - Dashboard de estadísticas

6. **Guía por Rol - Administrador** (con capturas de pantalla)
   - Dashboard general
   - Cómo aprobar/rechazar productos
   - Cómo gestionar usuarios
   - Cómo ver todas las órdenes
   - Estadísticas del sistema

7. **Preguntas Frecuentes**
   - ¿Qué hago si olvidé mi contraseña?
   - ¿Por qué mi producto no aparece en el catálogo?
   - ¿Cómo contacto con un vendedor?

### 9.2 Herramientas para Capturas

**En Android:**
- **Captura normal**: Presiona **Volumen Abajo + Botón de Encendido** al mismo tiempo
- **Captura con scroll**: Toma la captura y luego toca **"Captura extendida"** si está disponible

**Para editar las capturas:**
- Usa **Google Slides**, **PowerPoint** o **Canva**
- Agrega flechas, círculos y texto explicativo a cada captura
- Numera los pasos

---

## 📋 **PASO 10: Grabar el Video de Lanzamiento (1 minuto)**

### 10.1 Guion Sugerido para el Video

**Duración: 60 segundos**

```
[0-8 seg] Intro
- Pantalla de splash de la app
- Texto: "Print3D Store - Tu tienda de productos impresos en 3D"

[8-20 seg] Login y Registro
- Mostrar pantalla de login
- Registrarse rápidamente
- Login exitoso

[20-35 seg] Navegación del Catálogo (Comprador)
- Scroll por el catálogo de productos
- Tocar un producto para ver detalle
- Agregar al carrito

[35-45 seg] Crear Producto (Vendedor)
- Cerrar sesión rápido
- Login como vendedor
- Crear un producto nuevo con foto

[45-55 seg] Dashboard Admin
- Cerrar sesión rápido
- Login como admin
- Mostrar dashboard con estadísticas
- Aprobar un producto

[55-60 seg] Cierre
- Volver a catálogo de comprador
- Mostrar el producto recién aprobado
- Texto final: "Print3D Store - Innovación en 3D"
```

### 10.2 Consejos para Grabar

1. **Prepara el dispositivo**:
   - Limpia la pantalla
   - Pon el brillo al máximo
   - Desactiva notificaciones (Modo No Molestar)
   - Cierra otras apps
   - Carga la batería al 100%

2. **Graba la pantalla**:
   - **Android 11+**: Desliza hacia abajo, toca **"Grabar pantalla"**
   - Si no está, descarga **"AZ Screen Recorder"** (gratis)
   - Configura: **1080p, 60fps, sin audio** (o con música de fondo libre de derechos)

3. **Ensaya antes**:
   - Haz una grabación de prueba
   - Verifica que todo se vea bien
   - Cronometra que dure ~60 segundos

4. **Edita el video** (opcional):
   - Usa **CapCut** (gratis en celular)
   - Recorta las partes lentas
   - Agrega transiciones sutiles
   - Agrega texto explicativo
   - Agrega música de fondo (royalty-free de YouTube Audio Library)

### 10.3 Dónde subir el video

- **YouTube**: Como "No listado" (solo con el link)
- **Google Drive**: Con permisos de visualización
- **OneDrive** o **Dropbox**

---

## 📋 **PASO 11: Preparar la Entrega Final**

### 11.1 Archivos a entregar

Crea una carpeta llamada `Print3D_Store_Entrega` con:

```
Print3D_Store_Entrega/
├── Manual_Usuario_Print3D.pdf
├── Link_Video_Lanzamiento.txt (con el link de YouTube/Drive)
├── Link_Repositorio_GitHub.txt (https://github.com/yersonalarcon/Print3DStore)
└── Informe_Tecnico.pdf (opcional)
```

### 11.2 Contenido del Informe Técnico (si lo piden)

1. **Arquitectura del Proyecto**
   - Clean Architecture + MVVM
   - Capas: Presentation, Domain, Data

2. **Tecnologías Utilizadas**
   - Kotlin
   - Firebase Authentication
   - Cloud Firestore
   - Firebase Storage
   - Google Maps API
   - CameraX
   - ePayco (Pasarela de Pagos)
   - Material Design 3

3. **Funcionalidades Implementadas**
   - ✅ Sistema de autenticación con roles
   - ✅ CRUD de productos con aprobación de admin
   - ✅ CRUD de usuarios
   - ✅ CRUD de órdenes/pedidos
   - ✅ Carrito de compras
   - ✅ Captura de fotos con cámara
   - ✅ Geolocalización
   - ✅ Pasarela de pagos
   - ✅ Dashboard por rol

4. **Pruebas Realizadas**
   - Pruebas de login/registro
   - Pruebas de CRUDs
   - Pruebas de flujo completo de compra
   - Pruebas de permisos (cámara, ubicación)

5. **Conclusiones**
   - Logros del proyecto
   - Retos enfrentados
   - Aprendizajes

---

## 🔧 **SOLUCIÓN DE PROBLEMAS COMUNES**

### Problema 1: "google-services.json not found"

**Solución:**
1. Verifica que el archivo esté en `app/google-services.json`
2. Haz **Clean Project** (Build > Clean Project)
3. Haz **Rebuild Project** (Build > Rebuild Project)

### Problema 2: "Gradle sync failed"

**Solución:**
1. File > Invalidate Caches / Restart
2. Borra carpetas `.gradle` y `.idea`
3. Sync nuevamente

### Problema 3: "Firebase Auth failed"

**Solución:**
1. Verifica que Email/Password esté habilitado en Firebase Console
2. Verifica tu conexión a Internet
3. Revisa los logs de Android Studio (Logcat)

### Problema 4: "No aparecen productos en el catálogo"

**Solución:**
1. Verifica que hayas creado productos como vendedor
2. Verifica que un admin los haya **aprobado**
3. Ve a Firebase Console > Firestore > colección `products`
4. Verifica que haya documentos con `status: "APPROVED"`

### Problema 5: "La cámara no funciona"

**Solución:**
1. Verifica que hayas aceptado los permisos de cámara
2. Ve a Configuración del dispositivo > Apps > Print3D Store > Permisos
3. Activa el permiso de Cámara

### Problema 6: "La ubicación no se obtiene"

**Solución:**
1. Activa el GPS del dispositivo
2. Acepta los permisos de ubicación
3. Sal al exterior (mejor señal GPS)
4. Espera unos segundos

---

## 📞 **Recursos Adicionales**

- **Firebase Console**: https://console.firebase.google.com/
- **Google Cloud Console**: https://console.cloud.google.com/
- **Documentación Firebase Auth**: https://firebase.google.com/docs/auth/android/start
- **Documentación Firestore**: https://firebase.google.com/docs/firestore/quickstart
- **ePayco Docs**: https://docs.epayco.co/
- **Material Design 3**: https://m3.material.io/

---

## ✅ **Checklist Final**

Antes de entregar, verifica:

- [ ] El proyecto compila sin errores
- [ ] Probaste las 3 funcionalidades por rol (Comprador, Vendedor, Admin)
- [ ] La autenticación funciona (login, registro, logout)
- [ ] Se pueden crear productos con foto
- [ ] Los productos se aprueban correctamente
- [ ] El carrito de compras funciona
- [ ] Se pueden crear órdenes
- [ ] La geolocalización funciona
- [ ] Creaste el manual de usuario con capturas
- [ ] Grabaste el video de 1 minuto en dispositivo real
- [ ] Subiste el video a YouTube/Drive
- [ ] Preparaste los archivos de entrega

---

## 🎉 **¡Listo!**

Si llegaste hasta aquí y todo funciona, **¡FELICITACIONES!** 🎊

Has completado exitosamente el proyecto Print3D Store con todas sus funcionalidades.

**¡Mucha suerte con tu entrega!** 🚀
