package com.compensar.print3dstore.data.payment

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Implementación de la pasarela de pagos ePayco
 * https://docs.epayco.co/
 */
class EPaycoGateway(
    private val publicKey: String,
    private val privateKey: String,
    private val isTest: Boolean = true
) : PaymentGateway {
    
    private val baseUrl = if (isTest) {
        "https://secure.epayco.co"
    } else {
        "https://secure.epayco.co"
    }
    
    private val client: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Crea una transacción de pago con ePayco
     */
    override suspend fun createPayment(
        amount: Double,
        currency: String,
        description: String,
        buyerEmail: String
    ): PaymentResult = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("public_key", publicKey)
                put("name", "Print3D Store - $description")
                put("description", description)
                put("invoice", "INV-${System.currentTimeMillis()}")
                put("currency", currency)
                put("amount", amount.toString())
                put("tax_base", "0")
                put("tax", "0")
                put("country", "CO")
                put("lang", "es")
                
                // Información del comprador
                put("email_billing", buyerEmail)
                put("type_person", "0") // 0 = Persona natural
                
                // URL de confirmación (webhook)
                put("confirmation", "https://tuservidor.com/epayco/confirmation")
                put("url_response", "https://tuservidor.com/epayco/response")
                
                // Testing
                put("test", isTest.toString())
            }
            
            val body = json.toString().toRequestBody("application/json".toMediaType())
            
            val request = Request.Builder()
                .url("$baseUrl/checkout/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            if (response.isSuccessful && responseBody != null) {
                val jsonResponse = JSONObject(responseBody)
                
                if (jsonResponse.getBoolean("success")) {
                    val data = jsonResponse.getJSONObject("data")
                    PaymentResult(
                        success = true,
                        transactionId = data.optString("ref_payco"),
                        checkoutUrl = data.optString("url_payment"),
                        message = "Pago creado exitosamente"
                    )
                } else {
                    PaymentResult(
                        success = false,
                        transactionId = null,
                        checkoutUrl = null,
                        message = jsonResponse.optString("message", "Error al crear el pago")
                    )
                }
            } else {
                PaymentResult(
                    success = false,
                    transactionId = null,
                    checkoutUrl = null,
                    message = "Error en la respuesta del servidor: ${response.code}"
                )
            }
        } catch (e: Exception) {
            PaymentResult(
                success = false,
                transactionId = null,
                checkoutUrl = null,
                message = "Error: ${e.message}"
            )
        }
    }
    
    /**
     * Verifica el estado de una transacción
     */
    override suspend fun verifyPayment(transactionId: String): PaymentVerificationResult = 
        withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/payment/v1/reference/$transactionId")
                .get()
                .addHeader("Authorization", "Bearer $privateKey")
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            
            if (response.isSuccessful && responseBody != null) {
                val jsonResponse = JSONObject(responseBody)
                
                if (jsonResponse.getBoolean("success")) {
                    val data = jsonResponse.getJSONObject("data")
                    val status = data.optString("x_cod_transaction_state")
                    
                    val paymentStatus = when (status) {
                        "1" -> PaymentStatus.APPROVED
                        "2" -> PaymentStatus.REJECTED
                        "3" -> PaymentStatus.PENDING
                        "4" -> PaymentStatus.CANCELLED
                        else -> PaymentStatus.ERROR
                    }
                    
                    PaymentVerificationResult(
                        success = true,
                        status = paymentStatus,
                        message = "Verificación exitosa"
                    )
                } else {
                    PaymentVerificationResult(
                        success = false,
                        status = PaymentStatus.ERROR,
                        message = "Error al verificar el pago"
                    )
                }
            } else {
                PaymentVerificationResult(
                    success = false,
                    status = PaymentStatus.ERROR,
                    message = "Error en la respuesta del servidor"
                )
            }
        } catch (e: Exception) {
            PaymentVerificationResult(
                success = false,
                status = PaymentStatus.ERROR,
                message = "Error: ${e.message}"
            )
        }
    }
}

/**
 * Factory para crear instancia de ePayco
 */
object PaymentGatewayFactory {
    
    // Claves de prueba de ePayco
    private const val TEST_PUBLIC_KEY = "491d6a0b6e599087e52da47323e7cf75"
    private const val TEST_PRIVATE_KEY = "cda6df9a5a71f7f6fd91cd2db5c86abb"
    
    fun createEPayco(isTest: Boolean = true): PaymentGateway {
        return EPaycoGateway(
            publicKey = TEST_PUBLIC_KEY,
            privateKey = TEST_PRIVATE_KEY,
            isTest = isTest
        )
    }
}
