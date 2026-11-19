package com.compensar.print3dstore.data.repository

import com.compensar.print3dstore.data.firebase.FirebaseManager
import com.compensar.print3dstore.domain.model.User
import com.compensar.print3dstore.domain.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repositorio para manejar la autenticación de usuarios
 */
class AuthRepository {
    
    private val auth: FirebaseAuth = FirebaseManager.getAuth()
    private val firestore: FirebaseFirestore = FirebaseManager.getFirestore()
    
    /**
     * Registra un nuevo usuario
     */
    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String,
        role: UserRole
    ): Result<User> {
        return try {
            // Crear usuario en Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Error al crear usuario")
            
            // Actualizar perfil con el nombre
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build()
            firebaseUser.updateProfile(profileUpdates).await()
            
            // Crear documento de usuario en Firestore
            val user = User(
                id = firebaseUser.uid,
                email = email,
                fullName = fullName,
                phone = phone,
                role = role,
                createdAt = System.currentTimeMillis(),
                isActive = true
            )
            
            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(user)
                .await()
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Inicia sesión con email y contraseña
     */
    suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Autenticar con Firebase
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Usuario no encontrado")
            
            // Obtener datos del usuario desde Firestore
            val userDoc = firestore.collection("users")
                .document(firebaseUser.uid)
                .get()
                .await()
            
            val user = userDoc.toObject(User::class.java) 
                ?: throw Exception("Datos de usuario no encontrados")
            
            // Verificar que el usuario esté activo
            if (!user.isActive) {
                auth.signOut()
                throw Exception("Usuario inactivo. Contacta al administrador")
            }
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Cierra la sesión del usuario actual
     */
    fun logout() {
        auth.signOut()
    }
    
    /**
     * Obtiene el usuario actualmente autenticado
     */
    suspend fun getCurrentUser(): Result<User?> {
        return try {
            val firebaseUser = auth.currentUser
            if (firebaseUser == null) {
                Result.success(null)
            } else {
                val userDoc = firestore.collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .await()
                
                val user = userDoc.toObject(User::class.java)
                Result.success(user)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Envía email para recuperar contraseña
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Verifica si hay un usuario autenticado
     */
    fun isUserLoggedIn(): Boolean = auth.currentUser != null
    
    /**
     * Obtiene el ID del usuario actual
     */
    fun getCurrentUserId(): String? = auth.currentUser?.uid
}
