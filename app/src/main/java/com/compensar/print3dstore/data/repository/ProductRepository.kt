package com.compensar.print3dstore.data.repository

import android.net.Uri
import com.compensar.print3dstore.data.firebase.FirebaseManager
import com.compensar.print3dstore.domain.model.Product
import com.compensar.print3dstore.domain.model.ProductStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Repositorio para manejar operaciones CRUD de productos
 */
class ProductRepository {
    
    private val firestore: FirebaseFirestore = FirebaseManager.getFirestore()
    private val storage: FirebaseStorage = FirebaseManager.getStorage()
    
    /**
     * Crea un nuevo producto
     */
    suspend fun createProduct(product: Product, imageUri: Uri?): Result<Product> {
        return try {
            val productId = product.id.ifEmpty { UUID.randomUUID().toString() }
            var imageUrl = product.imageUrl
            
            // Subir imagen si existe
            if (imageUri != null) {
                imageUrl = uploadImage(productId, imageUri)
            }
            
            val newProduct = product.copy(
                id = productId,
                imageUrl = imageUrl,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            firestore.collection("products")
                .document(productId)
                .set(newProduct)
                .await()
            
            Result.success(newProduct)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene un producto por ID
     */
    suspend fun getProductById(productId: String): Result<Product?> {
        return try {
            val doc = firestore.collection("products")
                .document(productId)
                .get()
                .await()
            
            val product = doc.toObject(Product::class.java)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene todos los productos aprobados (para compradores)
     */
    suspend fun getAllApprovedProducts(): Result<List<Product>> {
        return try {
            val snapshot = firestore.collection("products")
                .whereEqualTo("status", ProductStatus.APPROVED.name)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val products = snapshot.documents.mapNotNull { 
                it.toObject(Product::class.java) 
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene productos de un vendedor específico
     */
    suspend fun getProductsBySeller(sellerId: String): Result<List<Product>> {
        return try {
            val snapshot = firestore.collection("products")
                .whereEqualTo("sellerId", sellerId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val products = snapshot.documents.mapNotNull { 
                it.toObject(Product::class.java) 
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene productos pendientes de aprobación (para admin)
     */
    suspend fun getPendingProducts(): Result<List<Product>> {
        return try {
            val snapshot = firestore.collection("products")
                .whereEqualTo("status", ProductStatus.PENDING.name)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val products = snapshot.documents.mapNotNull { 
                it.toObject(Product::class.java) 
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Actualiza un producto
     */
    suspend fun updateProduct(product: Product, newImageUri: Uri? = null): Result<Product> {
        return try {
            var imageUrl = product.imageUrl
            
            // Subir nueva imagen si existe
            if (newImageUri != null) {
                imageUrl = uploadImage(product.id, newImageUri)
            }
            
            val updatedProduct = product.copy(
                imageUrl = imageUrl,
                updatedAt = System.currentTimeMillis()
            )
            
            firestore.collection("products")
                .document(product.id)
                .set(updatedProduct)
                .await()
            
            Result.success(updatedProduct)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Elimina un producto
     */
    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            // Eliminar imagen de storage
            try {
                storage.reference.child("products/$productId.jpg").delete().await()
            } catch (e: Exception) {
                // Imagen no existe o ya fue eliminada
            }
            
            // Eliminar documento
            firestore.collection("products")
                .document(productId)
                .delete()
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Aprueba un producto (admin)
     */
    suspend fun approveProduct(productId: String): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(productId)
                .update(
                    mapOf(
                        "status" to ProductStatus.APPROVED.name,
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Rechaza un producto (admin)
     */
    suspend fun rejectProduct(productId: String): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(productId)
                .update(
                    mapOf(
                        "status" to ProductStatus.REJECTED.name,
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Busca productos por nombre
     */
    suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val snapshot = firestore.collection("products")
                .whereEqualTo("status", ProductStatus.APPROVED.name)
                .get()
                .await()
            
            val products = snapshot.documents
                .mapNotNull { it.toObject(Product::class.java) }
                .filter { it.name.contains(query, ignoreCase = true) }
            
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Sube una imagen a Firebase Storage
     */
    private suspend fun uploadImage(productId: String, imageUri: Uri): String {
        val storageRef = storage.reference.child("products/$productId.jpg")
        storageRef.putFile(imageUri).await()
        return storageRef.downloadUrl.await().toString()
    }
}
