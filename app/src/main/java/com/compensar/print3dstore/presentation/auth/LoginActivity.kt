package com.compensar.print3dstore.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.compensar.print3dstore.R
import com.compensar.print3dstore.databinding.ActivityLoginBinding
import com.compensar.print3dstore.domain.model.UserRole
import com.compensar.print3dstore.presentation.admin.AdminMainActivity
import com.compensar.print3dstore.presentation.buyer.BuyerMainActivity
import com.compensar.print3dstore.presentation.seller.SellerMainActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

/**
 * Activity para el Login de usuarios con Firebase Authentication
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
        observeViewModel()
        checkBiometricAvailability()
    }
    
    private fun setupViews() {
        // Botón de login
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            // Limpiar errores previos
            binding.tilEmail.error = null
            binding.tilPassword.error = null
            
            if (validateInputs(email, password)) {
                viewModel.login(email, password)
            }
        }
        
        // Botón biométrico
        binding.btnBiometric.setOnClickListener {
            showBiometricPrompt()
        }
        
        // Link para registrarse
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        
        // Link para recuperar contraseña
        binding.tvForgotPassword.setOnClickListener {
            showPasswordResetDialog()
        }
    }
    
    private fun observeViewModel() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    showLoading(true)
                }
                is AuthState.Success -> {
                    showLoading(false)
                    navigateToMainActivity(state.user.role)
                }
                is AuthState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                is AuthState.PasswordResetSent -> {
                    showLoading(false)
                    Toast.makeText(
                        this,
                        "Email de recuperación enviado. Revisa tu correo.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    
    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.error_empty_field)
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.error_invalid_email)
            return false
        }
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.error_empty_field)
            return false
        }
        if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.error_weak_password)
            return false
        }
        return true
    }
    
    private fun showPasswordResetDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_password_reset, null)
        val emailInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(
            R.id.et_reset_email
        )
        
        AlertDialog.Builder(this)
            .setTitle("Recuperar Contraseña")
            .setMessage("Ingresa tu email y te enviaremos un link para restablecer tu contraseña")
            .setView(dialogView)
            .setPositiveButton("Enviar") { _, _ ->
                val email = emailInput?.text?.toString()?.trim()
                if (!email.isNullOrEmpty() && 
                    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    viewModel.resetPassword(email)
                } else {
                    Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun checkBiometricAvailability() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.btnBiometric.isEnabled = true
            }
            else -> {
                binding.btnBiometric.isEnabled = false
                binding.btnBiometric.alpha = 0.5f
            }
        }
    }
    
    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    
                    // Aquí podrías cargar credenciales guardadas de forma segura
                    // Por ahora, solo mostramos éxito
                    Toast.makeText(
                        this@LoginActivity,
                        "Autenticación biométrica exitosa. Funcionalidad completa próximamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@LoginActivity, errString, Toast.LENGTH_SHORT).show()
                }
            })
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setDescription(getString(R.string.biometric_description))
            .setNegativeButtonText(getString(R.string.biometric_negative))
            .build()
        
        biometricPrompt.authenticate(promptInfo)
    }
    
    private fun navigateToMainActivity(role: UserRole) {
        val intent = when (role) {
            UserRole.BUYER -> Intent(this, BuyerMainActivity::class.java)
            UserRole.SELLER -> Intent(this, SellerMainActivity::class.java)
            UserRole.ADMIN -> Intent(this, AdminMainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
    
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
        binding.btnBiometric.isEnabled = !isLoading && binding.btnBiometric.alpha == 1f
    }
}
