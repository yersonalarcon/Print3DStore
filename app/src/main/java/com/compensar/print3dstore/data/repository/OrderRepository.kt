package com.compensar.print3dstore.data.repository

import com.compensar.print3dstore.data.firebase.FirebaseManager
import com.compensar.print3dstore.domain.model.Order
import com.compensar.print3dstore.domain.model.OrderStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Repositorio para manejar operaciones CRUD de órdenes/pedidos
 */
class OrderRepository {
    
    private val firestore: FirebaseFirestore = FirebaseManager.getFirestore()
    
    /**
     * Crea una nueva orden
     */
    suspend fun createOrder(order: Order): Result<Order> {
        return try {
            val orderId = order.id.ifEmpty { UUID.randomUUID().toString() }
            
            val newOrder = order.copy(
                id = orderId,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            firestore.collection("orders")
                .document(orderId)
                .set(newOrder)
                .await()
            
            Result.success(newOrder)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene una orden por ID
     */
    suspend fun getOrderById(orderId: String): Result<Order?> {
        return try {
            val doc = firestore.collection("orders")
                .document(orderId)
                .get()
                .await()
            
            val order = doc.toObject(Order::class.java)
            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene todas las órdenes de un comprador
     */
    suspend fun getOrdersByBuyer(buyerId: String): Result<List<Order>> {
        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("buyerId", buyerId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val orders = snapshot.documents.mapNotNull { 
                it.toObject(Order::class.java) 
            }
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene todas las órdenes (para admin)
     */
    suspend fun getAllOrders(): Result<List<Order>> {
        return try {
            val snapshot = firestore.collection("orders")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            val orders = snapshot.documents.mapNotNull { 
                it.toObject(Order::class.java) 
            }
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene órdenes que contienen productos de un vendedor
     */
    suspend fun getOrdersBySeller(sellerId: String): Result<List<Order>> {
        return try {
            val snapshot = firestore.collection("orders")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            
            // Filtrar órdenes que contienen productos del vendedor
            val orders = snapshot.documents
                .mapNotNull { it.toObject(Order::class.java) }
                .filter { order ->
                    order.items.any { item -> item.sellerId == sellerId }
                }
            
            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Actualiza el estado de una orden
     */
    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Result<Unit> {
        return try {
            firestore.collection("orders")
                .document(orderId)
                .update(
                    mapOf(
                        "status" to newStatus.name,
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
     * Cancela una orden
     */
    suspend fun cancelOrder(orderId: String): Result<Unit> {
        return updateOrderStatus(orderId, OrderStatus.CANCELLED)
    }
    
    /**
     * Elimina una orden
     */
    suspend fun deleteOrder(orderId: String): Result<Unit> {
        return try {
            firestore.collection("orders")
                .document(orderId)
                .delete()
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene estadísticas de ventas para el dashboard del vendedor
     */
    suspend fun getSellerStats(sellerId: String): Result<Map<String, Any>> {
        return try {
            val orders = getOrdersBySeller(sellerId).getOrNull() ?: emptyList()
            
            val totalOrders = orders.size
            val completedOrders = orders.count { it.status == OrderStatus.DELIVERED }
            val totalRevenue = orders
                .filter { it.status == OrderStatus.DELIVERED }
                .sumOf { it.total }
            
            val stats = mapOf(
                "totalOrders" to totalOrders,
                "completedOrders" to completedOrders,
                "pendingOrders" to orders.count { it.status == OrderStatus.PENDING },
                "totalRevenue" to totalRevenue
            )
            
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene estadísticas generales para el dashboard del admin
     */
    suspend fun getAdminStats(): Result<Map<String, Any>> {
        return try {
            val orders = getAllOrders().getOrNull() ?: emptyList()
            
            val totalOrders = orders.size
            val completedOrders = orders.count { it.status == OrderStatus.DELIVERED }
            val totalRevenue = orders
                .filter { it.status == OrderStatus.DELIVERED }
                .sumOf { it.total }
            
            val stats = mapOf(
                "totalOrders" to totalOrders,
                "completedOrders" to completedOrders,
                "pendingOrders" to orders.count { it.status == OrderStatus.PENDING },
                "totalRevenue" to totalRevenue
            )
            
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
