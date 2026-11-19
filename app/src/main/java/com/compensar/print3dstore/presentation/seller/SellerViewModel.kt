package com.compensar.print3dstore.presentation.seller

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compensar.print3dstore.data.repository.OrderRepository
import com.compensar.print3dstore.data.repository.ProductRepository
import com.compensar.print3dstore.domain.model.Order
import com.compensar.print3dstore.domain.model.Product
import com.compensar.print3dstore.domain.model.ProductStatus
import kotlinx.coroutines.launch

/**
 * ViewModel para la lógica del Vendedor
 */
class SellerViewModel : ViewModel() {
    
    private val productRepository = ProductRepository()
    private val orderRepository = OrderRepository()
    
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
    
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders
    
    private val _stats = MutableLiveData<Map<String, Any>>()
    val stats: LiveData<Map<String, Any>> = _stats
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _productSaved = MutableLiveData<Boolean>()
    val productSaved: LiveData<Boolean> = _productSaved
    
    /**
     * Carga los productos del vendedor
     */
    fun loadProducts(sellerId: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = productRepository.getProductsBySeller(sellerId)
            _loading.value = false
            
            if (result.isSuccess) {
                _products.value = result.getOrNull() ?: emptyList()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Crea un nuevo producto
     */
    fun createProduct(
        name: String,
        description: String,
        price: Double,
        stock: Int,
        category: String,
        sellerId: String,
        imageUri: Uri?
    ) {
        // Validaciones
        if (name.isBlank()) {
            _error.value = "El nombre del producto es requerido"
            return
        }
        
        if (price <= 0) {
            _error.value = "El precio debe ser mayor a 0"
            return
        }
        
        if (stock < 0) {
            _error.value = "El stock no puede ser negativo"
            return
        }
        
        _loading.value = true
        
        val product = Product(
            id = "",
            name = name,
            description = description,
            price = price,
            imageUrl = "",
            category = category,
            sellerId = sellerId,
            stock = stock,
            status = ProductStatus.PENDING,
            rating = 0.0,
            reviewCount = 0,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        viewModelScope.launch {
            val result = productRepository.createProduct(product, imageUri)
            _loading.value = false
            
            if (result.isSuccess) {
                _productSaved.value = true
                loadProducts(sellerId)
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Actualiza un producto existente
     */
    fun updateProduct(product: Product, newImageUri: Uri? = null) {
        _loading.value = true
        viewModelScope.launch {
            val result = productRepository.updateProduct(product, newImageUri)
            _loading.value = false
            
            if (result.isSuccess) {
                _productSaved.value = true
                loadProducts(product.sellerId)
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Elimina un producto
     */
    fun deleteProduct(productId: String, sellerId: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = productRepository.deleteProduct(productId)
            _loading.value = false
            
            if (result.isSuccess) {
                loadProducts(sellerId)
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Carga las órdenes del vendedor
     */
    fun loadOrders(sellerId: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = orderRepository.getOrdersBySeller(sellerId)
            _loading.value = false
            
            if (result.isSuccess) {
                _orders.value = result.getOrNull() ?: emptyList()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Carga estadísticas del vendedor
     */
    fun loadStats(sellerId: String) {
        viewModelScope.launch {
            val result = orderRepository.getSellerStats(sellerId)
            
            if (result.isSuccess) {
                _stats.value = result.getOrNull() ?: emptyMap()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _error.value = null
    }
    
    /**
     * Resetea el flag de producto guardado
     */
    fun resetProductSaved() {
        _productSaved.value = false
    }
}
