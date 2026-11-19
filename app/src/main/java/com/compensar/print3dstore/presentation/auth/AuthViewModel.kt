package com.compensar.print3dstore.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compensar.print3dstore.data.repository.AuthRepository
import com.compensar.print3dstore.domain.model.User
import com.compensar.print3dstore.domain.model.UserRole
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica de autenticación
 */
class AuthViewModel : ViewModel() {
    
    private val authRepository = AuthRepository()
    
    private val _loginState = MutableLiveData<AuthState>()
    val loginState: LiveData<AuthState> = _loginState
    
    private val _registerState = MutableLiveData<AuthState>()
    val registerState: LiveData<AuthState> = _registerState
    
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser
    
    /**
     * Inicia sesión con email y contraseña
     */
    fun login(email: String, password: String) {
        if (!validateEmail(email)) {
            _loginState.value = AuthState.Error("Email inválido")
            return
        }
        
        if (password.length < 6) {
            _loginState.value = AuthState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }
        
        _loginState.value = AuthState.Loading
        
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _loginState.value = if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
                AuthState.Success(result.getOrNull()!!)
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error al iniciar sesión")
            }
        }
    }
    
    /**
     * Registra un nuevo usuario
     */
    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        fullName: String,
        phone: String,
        role: UserRole
    ) {
        // Validaciones
        if (!validateEmail(email)) {
            _registerState.value = AuthState.Error("Email inválido")
            return
        }
        
        if (password.length < 6) {
            _registerState.value = AuthState.Error("La contraseña debe tener al menos 6 caracteres")
            return
        }
        
        if (password != confirmPassword) {
            _registerState.value = AuthState.Error("Las contraseñas no coinciden")
            return
        }
        
        if (fullName.isBlank()) {
            _registerState.value = AuthState.Error("El nombre completo es requerido")
            return
        }
        
        if (phone.isBlank()) {
            _registerState.value = AuthState.Error("El teléfono es requerido")
            return
        }
        
        _registerState.value = AuthState.Loading
        
        viewModelScope.launch {
            val result = authRepository.register(email, password, fullName, phone, role)
            _registerState.value = if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
                AuthState.Success(result.getOrNull()!!)
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error al registrarse")
            }
        }
    }
    
    /**
     * Cierra la sesión del usuario actual
     */
    fun logout() {
        authRepository.logout()
        _currentUser.value = null
    }
    
    /**
     * Obtiene el usuario actualmente autenticado
     */
    fun loadCurrentUser() {
        viewModelScope.launch {
            val result = authRepository.getCurrentUser()
            if (result.isSuccess) {
                _currentUser.value = result.getOrNull()
            }
        }
    }
    
    /**
     * Envía email para recuperar contraseña
     */
    fun resetPassword(email: String) {
        if (!validateEmail(email)) {
            _loginState.value = AuthState.Error("Email inválido")
            return
        }
        
        _loginState.value = AuthState.Loading
        
        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            _loginState.value = if (result.isSuccess) {
                AuthState.PasswordResetSent
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error al enviar email")
            }
        }
    }
    
    /**
     * Valida formato de email
     */
    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    /**
     * Verifica si hay un usuario autenticado
     */
    fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()
}

/**
 * Estados posibles de la autenticación
 */
sealed class AuthState {
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
    object PasswordResetSent : AuthState()
}
