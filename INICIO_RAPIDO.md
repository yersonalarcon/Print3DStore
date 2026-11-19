# ⚡ Inicio Rápido - Print3D Store

## 🔥 Solo 3 pasos para empezar

### PASO 1: Configurar Firebase (10 minutos)

1. **Crear proyecto**: https://console.firebase.google.com/
   - Nombre: `Print3D-Store`
   - NO activar Analytics

2. **Agregar Android App**:
   - Package name: `com.compensar.print3dstore`
   - **Descargar `google-services.json`**
   - **Copiar** ese archivo a: `Print3DStore/app/google-services.json`

3. **Habilitar servicios**:
   - ✅ **Authentication** > Email/Password (activar)
   - ✅ **Firestore Database** > Crear en modo prueba
   - ✅ **Storage** > Crear en modo prueba

### PASO 2: Google Maps API (5 minutos)

1. Ir a: https://console.cloud.google.com/
2. Seleccionar tu proyecto de Firebase
3. **APIs & Services** > **Credentials** > **+ CREATE CREDENTIALS** > **API key**
4. Copiar la API Key
5. Abrir archivo `local.properties` y agregar:
   ```
   MAPS_API_KEY=TU_API_KEY_AQUI
   ```

6. **APIs & Services** > **Library** > Habilitar:
   - Maps SDK for Android
   - Places API

### PASO 3: Ejecutar (2 minutos)

1. Abrir Android Studio
2. **Sync Project with Gradle Files** (icono de elefante)
3. Conectar dispositivo físico con depuración USB activada
4. Click en **▶ Run**
5. ¡Listo! La app se instalará

---

## 🧪 Probar la App

### Crear usuarios de prueba:

1. **Comprador**:
   - Email: `comprador@print3d.com`
   - Pass: `123456`

2. **Vendedor**:
   - Email: `vendedor@print3d.com`
   - Pass: `123456`

3. **Admin**:
   - Primero regístralo como Comprador
   - Luego en Firebase Console > Firestore > users
   - Edita el campo `role` a `ADMIN`

---

## 📝 Para la entrega necesitas:

1. ✅ **Manual de usuario** (PDF con capturas por rol)
2. ✅ **Video de 1 minuto** en dispositivo real
3. ✅ Link del repositorio GitHub

**Para más detalles**, ve a `GUIA_CONFIGURACION_COMPLETA.md`

---

## 🆘 ¿Problemas?

### Error: "google-services.json not found"
➡️ Verifica que esté en `app/google-services.json` (dentro de carpeta app)

### Error: "Gradle sync failed"  
➡️ File > Invalidate Caches / Restart

### Firebase Auth no funciona
➡️ Verifica que Email/Password esté habilitado en Firebase Console

---

## 📧 Contacto

Si tienes dudas, puedes preguntarme y te ayudo a resolverlas.

**¡Éxito con tu proyecto!** 🚀
