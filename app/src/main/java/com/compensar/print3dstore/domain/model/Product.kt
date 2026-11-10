package com.compensar.print3dstore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val productId: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val categoryId: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val imageUrl: String = "",
    val customizationOptions: CustomizationOptions = CustomizationOptions(),
    val isActive: Boolean = true,
    val isPendingApproval: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class CustomizationOptions(
    val materials: List<String> = listOf("PLA", "ABS", "PETG"),
    val colors: List<String> = listOf("#1E3A8A", "#059669", "#F97316", "#DC2626"),
    val fillOptions: List<String> = listOf("10%", "20%", "50%", "100%"),
    val scales: List<String> = listOf("50%", "75%", "100%", "150%")
) : Parcelable

enum class ProductCategory(val displayName: String) {
    ARTICULATED("Articulados"),
    DECORATION("Decoración"),
    PROPS("Props"),
    REPLICAS("Réplicas"),
    FUNCTIONAL("Funcionales");
    
    companion object {
        fun fromId(id: String): ProductCategory {
            return values().find { it.name == id } ?: ARTICULATED
        }
    }
}
