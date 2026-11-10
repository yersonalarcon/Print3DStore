package com.compensar.print3dstore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val orderId: String = "",
    val buyerId: String = "",
    val buyerName: String = "",
    val sellerId: String = "",
    val items: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val shippingCost: Double = 0.0,
    val total: Double = 0.0,
    val status: OrderStatus = OrderStatus.PENDING,
    val deliveryAddress: DeliveryAddress = DeliveryAddress(),
    val paymentMethod: String = "Wompi",
    val transactionId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class DeliveryAddress(
    val addressText: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val city: String = "Bogotá"
) : Parcelable

enum class OrderStatus(val displayName: String) {
    PENDING("Pendiente"),
    PAID("Pagado"),
    PRINTING("En Impresión"),
    SHIPPED("Enviado"),
    DELIVERED("Entregado"),
    CANCELLED("Cancelado");
    
    fun getStatusColor(): String = when(this) {
        PENDING -> "#F59E0B"
        PAID -> "#3B82F6"
        PRINTING -> "#8B5CF6"
        SHIPPED -> "#059669"
        DELIVERED -> "#10B981"
        CANCELLED -> "#DC2626"
    }
}
