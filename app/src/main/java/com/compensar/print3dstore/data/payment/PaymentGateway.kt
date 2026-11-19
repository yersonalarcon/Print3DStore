package com.compensar.print3dstore.data.payment

/**
 * Interface para pasarela de pagos
 */
interface PaymentGateway {
    suspend fun createPayment(
        amount: Double,
        currency: String,
        description: String,
        buyerEmail: String
    ): PaymentResult
    
    suspend fun verifyPayment(transactionId: String): PaymentVerificationResult
}

/**
 * Resultado de creación de pago
 */
data class PaymentResult(
    val success: Boolean,
    val transactionId: String?,
    val checkoutUrl: String?,
    val message: String?
)

/**
 * Resultado de verificación de pago
 */
data class PaymentVerificationResult(
    val success: Boolean,
    val status: PaymentStatus,
    val message: String?
)

/**
 * Estados de pago
 */
enum class PaymentStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED,
    ERROR
}
