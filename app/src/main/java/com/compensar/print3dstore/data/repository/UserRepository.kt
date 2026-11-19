package com.compensar.print3dstore.data.repository

import com.compensar.print3dstore.data.firebase.FirebaseManager
import com.compensar.print3dstore.domain.model.User
import com.compensar.print3dstore.domain.model.UserRole
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repositorio para manejar operaciones CRUD de usuarios
 */
class UserRepository {
    
    private val firestore: FirebaseFirestore = FirebaseManager.getFirestore()
    
    /**
     * Obtiene un usuario por ID
     */
    suspend fun getUserById(userId: String): Result<User?> {
        return try {
            val doc = firestore.collection("users")
                .document(userId)
                .get()
                .await()
            
            val user = doc.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene todos los usuarios (para admin)
     */
    suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val snapshot = firestore.collection("users")
                .get()
                .await()
            
            val users = snapshot.documents.mapNotNull { 
                it.toObject(User::class.java) 
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene usuarios por rol
     */
    suspend fun getUsersByRole(role: UserRole): Result<List<User>> {
        return try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("role", role.name)
                .get()
                .await()
            
            val users = snapshot.documents.mapNotNull { 
                it.toObject(User::class.java) 
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Actualiza un usuario
     */
    suspend fun updateUser(user: User): Result<User> {
        return try {
            firestore.collection("users")
                .document(user.id)
                .set(user)
                .await()
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Activa un usuario
     */
    suspend fun activateUser(userId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("isActive", true)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Desactiva un usuario
     */
    suspend fun deactivateUser(userId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .update("isActive", false)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Elimina un usuario
     */
    suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            firestore.collection("users")
                .document(userId)
                .delete()
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene estadísticas de usuarios para el dashboard del admin
     */
    suspend fun getUserStats(): Result<Map<String, Int>> {
        return try {
            val users = getAllUsers().getOrNull() ?: emptyList()
            
            val stats = mapOf(
                "total" to users.size,
                "buyers" to users.count { it.role == UserRole.BUYER },
                "sellers" to users.count { it.role == UserRole.SELLER },
                "admins" to users.count { it.role == UserRole.ADMIN },
                "active" to users.count { it.isActive },
                "inactive" to users.count { !it.isActive }
            )
            
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
