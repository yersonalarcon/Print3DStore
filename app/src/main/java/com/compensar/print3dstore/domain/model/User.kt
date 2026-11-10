package com.compensar.print3dstore.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.BUYER,
    val profileImageUrl: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val address: String = ""
) : Parcelable

enum class UserRole {
    BUYER,
    SELLER,
    ADMIN;
    
    fun getDisplayName(): String = when(this) {
        BUYER -> "Comprador"
        SELLER -> "Vendedor"
        ADMIN -> "Administrador"
    }
}
