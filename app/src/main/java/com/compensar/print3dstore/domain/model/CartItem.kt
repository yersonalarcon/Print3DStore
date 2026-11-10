package com.compensar.print3dstore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val productId: String = "",
    val productTitle: String = "",
    val productImageUrl: String = "",
    val basePrice: Double = 0.0,
    val quantity: Int = 1,
    val selectedColor: String = "#1E3A8A",
    val selectedMaterial: String = "PLA",
    val selectedFill: String = "20%",
    val selectedScale: String = "100%",
    val customizationCost: Double = 0.0
) : Parcelable {
    fun getTotalPrice(): Double = (basePrice + customizationCost) * quantity
}

@Parcelize
data class Cart(
    val cartId: String = "",
    val buyerId: String = "",
    val items: List<CartItem> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable {
    fun getSubtotal(): Double = items.sumOf { it.getTotalPrice() }
    fun getItemCount(): Int = items.sumOf { it.quantity }
}
