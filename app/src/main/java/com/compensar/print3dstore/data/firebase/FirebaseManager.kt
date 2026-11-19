package com.compensar.print3dstore.data.firebase

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * Singleton para gestionar instancias de Firebase
 */
object FirebaseManager {
    
    private var initialized = false
    
    /**
     * Inicializa Firebase en la aplicación
     */
    fun initialize(context: Context) {
        if (!initialized) {
            try {
                FirebaseApp.initializeApp(context)
                initialized = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Obtiene la instancia de Firebase Authentication
     */
    fun getAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    
    /**
     * Obtiene la instancia de Firestore
     */
    fun getFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    
    /**
     * Obtiene la instancia de Firebase Storage
     */
    fun getStorage(): FirebaseStorage = FirebaseStorage.getInstance()
    
    /**
     * Obtiene el ID del usuario actualmente autenticado
     */
    fun getCurrentUserId(): String? = getAuth().currentUser?.uid
    
    /**
     * Verifica si hay un usuario autenticado
     */
    fun isUserLoggedIn(): Boolean = getAuth().currentUser != null
    
    /**
     * Cierra la sesión del usuario actual
     */
    fun signOut() {
        getAuth().signOut()
    }
}
