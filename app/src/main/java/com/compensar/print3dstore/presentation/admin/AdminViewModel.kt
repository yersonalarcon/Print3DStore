package com.compensar.print3dstore.presentation.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compensar.print3dstore.data.repository.OrderRepository
import com.compensar.print3dstore.data.repository.ProductRepository
import com.compensar.print3dstore.data.repository.UserRepository
import com.compensar.print3dstore.domain.model.Order
import com.compensar.print3dstore.domain.model.Product
import com.compensar.print3dstore.domain.model.User
import kotlinx.coroutines.launch

/**
 * ViewModel para la lógica del Administrador
 */
class AdminViewModel : ViewModel() {
    
    private val userRepository = UserRepository()
    private val productRepository = ProductRepository()
    private val orderRepository = OrderRepository()
    
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    
    private val _pendingProducts = MutableLiveData<List<Product>>()
    val pendingProducts: LiveData<List<Product>> = _pendingProducts
    
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders
    
    private val _userStats = MutableLiveData<Map<String, Int>>()
    val userStats: LiveData<Map<String, Int>> = _userStats
    
    private val _orderStats = MutableLiveData<Map<String, Any>>()
    val orderStats: LiveData<Map<String, Any>> = _orderStats
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    /**
     * Carga todos los usuarios
     */
    fun loadUsers() {
        _loading.value = true
        viewModelScope.launch {
            val result = userRepository.getAllUsers()
            _loading.value = false
            
            if (result.isSuccess) {
                _users.value = result.getOrNull() ?: emptyList()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Activa un usuario
     */
    fun activateUser(userId: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = userRepository.activateUser(userId)
            _loading.value = false
            
            if (result.isSuccess) {
                loadUsers()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Desactiva un usuario
     */
    fun deactivateUser(userId: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = userRepository.deactivateUser(userId)
            _loading.value = false
            
            if (result.isSuccess) {
                loadUsers()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Elimina un usuario
     */
    fun deleteUser(userId: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = userRepository.deleteUser(userId)
            _loading.value = false
            
            if (result.isSuccess) {
                loadUsers()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Carga los productos pendientes de aprobación
     */
    fun loadPendingProducts() {
        _loading.value = true
        viewModelScope.launch {
            val result = productRepository.getPendingProducts()
            _loading.value = false
            
            if (result.isSuccess) {
                _pendingProducts.value = result.getOrNull() ?: emptyList()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Aprueba un producto
     */
    fun approveProduct(productId: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = productRepository.approveProduct(productId)
            _loading.value = false
            
            if (result.isSuccess) {
                loadPendingProducts()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Rechaza un producto
     */
    fun rejectProduct(productId: String) {
        _loading.value = true
        viewModelScope.launch {
            val result = productRepository.rejectProduct(productId)
            _loading.value = false
            
            if (result.isSuccess) {
                loadPendingProducts()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Carga todas las órdenes
     */
    fun loadOrders() {
        _loading.value = true
        viewModelScope.launch {
            val result = orderRepository.getAllOrders()
            _loading.value = false
            
            if (result.isSuccess) {
                _orders.value = result.getOrNull() ?: emptyList()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Carga estadísticas de usuarios
     */
    fun loadUserStats() {
        viewModelScope.launch {
            val result = userRepository.getUserStats()
            
            if (result.isSuccess) {
                _userStats.value = result.getOrNull() ?: emptyMap()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Carga estadísticas de órdenes
     */
    fun loadOrderStats() {
        viewModelScope.launch {
            val result = orderRepository.getAdminStats()
            
            if (result.isSuccess) {
                _orderStats.value = result.getOrNull() ?: emptyMap()
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }
    
    /**
     * Carga todas las estadísticas del dashboard
     */
    fun loadDashboardStats() {
        loadUserStats()
        loadOrderStats()
    }
    
    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _error.value = null
    }
}
