package com.compensar.print3dstore.presentation.buyer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compensar.print3dstore.data.repository.OrderRepository
import com.compensar.print3dstore.data.repository.ProductRepository
import com.compensar.print3dstore.domain.model.CartItem
import com.compensar.print3dstore.domain.model.Order
import com.compensar.print3dstore.domain.model.OrderStatus
import com.compensar.print3dstore.domain.model.Product
import kotlinx.coroutines.launch

/**
 * ViewModel para la lógica del Comprador
 */
class BuyerViewModel : ViewModel() {
    
    private val productRepository = ProductRepository()
    private val orderRepository = OrderRepository()
    
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
    
    private val _cart = MutableLiveData<List<CartItem>>(emptyList())
    val cart: LiveData<List<CartItem>> = _cart
    
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders
    
    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> = _selectedProduct
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _orderCreated = MutableLiveData<Boolean>()
    val orderCreated: LiveData<Boolean> = _orderCreated
    
    /**
     * Carga todos los productos aprobados
     */
    fun loadProducts() {
        _loading.value = true
        viewModelScope.launch {
            val result = productRepository.getAllApprovedProducts()
            _loading.value = false
            
            if (result.isSuccess) {
                _products.value = result.getOrNull() ?: emptyList()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Busca productos por nombre
     */
    fun searchProducts(query: String) {
        if (query.isBlank()) {
            loadProducts()
            return
        }
        
        _loading.value = true
        viewModelScope.launch {
            val result = productRepository.searchProducts(query)
            _loading.value = false
            
            if (result.isSuccess) {
                _products.value = result.getOrNull() ?: emptyList()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Selecciona un producto para ver detalle
     */
    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }
    
    /**
     * Agrega un producto al carrito
     */
    fun addToCart(product: Product, quantity: Int) {
        val currentCart = _cart.value ?: emptyList()
        val existingItem = currentCart.find { it.productId == product.id }
        
        val updatedCart = if (existingItem != null) {
            // Actualizar cantidad si ya existe
            currentCart.map {
                if (it.productId == product.id) {
                    it.copy(quantity = it.quantity + quantity)
                } else {
                    it
                }
            }
        } else {
            // Agregar nuevo item
            currentCart + CartItem(
                productId = product.id,
                productName = product.name,
                productImage = product.imageUrl,
                price = product.price,
                quantity = quantity,
                sellerId = product.sellerId
            )
        }
        
        _cart.value = updatedCart
    }
    
    /**
     * Actualiza la cantidad de un item en el carrito
     */
    fun updateCartItemQuantity(productId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(productId)
            return
        }
        
        val updatedCart = _cart.value?.map {
            if (it.productId == productId) {
                it.copy(quantity = newQuantity)
            } else {
                it
            }
        }
        _cart.value = updatedCart
    }
    
    /**
     * Elimina un producto del carrito
     */
    fun removeFromCart(productId: String) {
        val updatedCart = _cart.value?.filter { it.productId != productId }
        _cart.value = updatedCart
    }
    
    /**
     * Vacía el carrito
     */
    fun clearCart() {
        _cart.value = emptyList()
    }
    
    /**
     * Calcula el total del carrito
     */
    fun getCartTotal(): Double {
        return _cart.value?.sumOf { it.price * it.quantity } ?: 0.0
    }
    
    /**
     * Crea una orden con los items del carrito
     */
    fun createOrder(buyerId: String, deliveryAddress: String, paymentMethod: String) {
        val cartItems = _cart.value
        if (cartItems.isNullOrEmpty()) {
            _error.value = "El carrito está vacío"
            return
        }
        
        _loading.value = true
        
        val order = Order(
            id = "",
            buyerId = buyerId,
            items = cartItems,
            total = getCartTotal(),
            status = OrderStatus.PENDING,
            deliveryAddress = deliveryAddress,
            paymentMethod = paymentMethod,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        viewModelScope.launch {
            val result = orderRepository.createOrder(order)
            _loading.value = false
            
            if (result.isSuccess) {
                clearCart()
                _orderCreated.value = true
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Carga las órdenes del comprador
     */
    fun loadOrders(buyerId: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = orderRepository.getOrdersByBuyer(buyerId)
            _loading.value = false
            
            if (result.isSuccess) {
                _orders.value = result.getOrNull() ?: emptyList()
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
}
