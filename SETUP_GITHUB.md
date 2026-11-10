# 🚀 Configuración de GitHub para Print3D Store

## Paso 1: Crear repositorio en GitHub

1. Ve a https://github.com/new
2. Nombre del repositorio: `Print3DStore`
3. Descripción: `Plataforma de comercio electrónico para productos 3D personalizados - Android Nativa con Kotlin`
4. Selecciona **Público** o **Privado** según prefieras
5. **NO** marques "Add README", "Add .gitignore" o "Add license" (ya los tenemos)
6. Click en **"Create repository"**

## Paso 2: Inicializar Git localmente

Abre la terminal en la carpeta del proyecto y ejecuta:

```bash
cd Print3DStore
git init
git add .
git commit -m "Initial commit: Print3D Store Android App - Complete UI with XML layouts for Buyer, Seller and Admin roles"
```

## Paso 3: Conectar con GitHub

Reemplaza `TU-USUARIO` con tu nombre de usuario de GitHub:

```bash
git remote add origin https://github.com/TU-USUARIO/Print3DStore.git
git branch -M main
git push -u origin main
```

## Paso 4: Verificar subida

1. Ve a https://github.com/TU-USUARIO/Print3DStore
2. Deberías ver todos los archivos del proyecto
3. El README.md se mostrará automáticamente en la página principal

## 📁 Estructura del Repositorio

```
Print3DStore/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/compensar/print3dstore/
│   │   │   │   ├── domain/model/          # Modelos de datos
│   │   │   │   └── presentation/
│   │   │   │       ├── auth/              # Login y Registro
│   │   │   │       ├── buyer/             # Pantallas Comprador
│   │   │   │       ├── seller/            # Pantallas Vendedor
│   │   │   │       ├── admin/             # Pantallas Admin
│   │   │   │       └── splash/            # Splash Screen
│   │   │   ├── res/
│   │   │   │   ├── layout/                # 15+ XML layouts
│   │   │   │   ├── drawable/              # Iconos y shapes
│   │   │   │   ├── values/                # Colors, strings, themes
│   │   │   │   └── menu/                  # Bottom navigation
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
├── gradle.properties
├── .gitignore
└── README.md
```

## 🔧 Configuración Adicional Necesaria

### Firebase (IMPORTANTE)

1. **Crear proyecto en Firebase Console**
   - Ve a https://console.firebase.google.com
   - Click en "Add project"
   - Nombre: `Print3D Store`
   
2. **Agregar aplicación Android**
   - Package name: `com.compensar.print3dstore`
   - Descargar `google-services.json`
   - Colocar en `app/google-services.json`
   
3. **Habilitar servicios**:
   - Authentication > Email/Password
   - Firestore Database
   - Storage

### Google Maps API Key

1. Ve a https://console.cloud.google.com
2. Crear API Key para Maps SDK for Android
3. Crear archivo `local.properties` en la raíz:
   ```properties
   sdk.dir=/ruta/a/tu/android/sdk
   MAPS_API_KEY=TU_API_KEY_AQUI
   ```

## 📝 Comandos Git Útiles

```bash
# Ver estado
git status

# Agregar cambios
git add .

# Commit
git commit -m "Descripción de cambios"

# Push
git push origin main

# Ver historial
git log --oneline

# Crear rama nueva
git checkout -b feature/nueva-funcionalidad

# Ver ramas
git branch

# Cambiar de rama
git checkout main
```

## 🎯 Próximos Pasos

1. ✅ Subir a GitHub
2. ⬜ Configurar Firebase
3. ⬜ Configurar Google Maps
4. ⬜ Probar compilación en Android Studio
5. ⬜ Generar APK de prueba
6. ⬜ Implementar ViewModels y Repositories
7. ⬜ Integrar Wompi para pagos
8. ⬜ Agregar pruebas unitarias

## 📞 Ayuda

Si tienes problemas:
1. Revisa que Git esté instalado: `git --version`
2. Verifica tu usuario de GitHub: `git config user.name`
3. Asegúrate de tener permisos en el repositorio
4. Si hay conflictos, usa: `git pull origin main` antes de `git push`

---

**Desarrollado por:** Yerson Alarcón  
**Institución:** Fundación Universitaria Compensar  
**Año:** 2026
