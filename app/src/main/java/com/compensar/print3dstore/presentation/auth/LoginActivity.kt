package com.compensar.print3dstore.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    // private val auth = FirebaseAuth.getInstance() // Comentado para compilación sin Firebase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
        checkBiometricAvailability()
    }
    
    private fun setupViews() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (validateInputs(email, password)) {
                // Navegación de demostración sin Firebase
                navigateToMainActivity(UserRole.BUYER)
            }
        }
        
        binding.btnBiometric.setOnClickListener {
            showBiometricPrompt()
        }
        
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        
        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(
                        this@LoginActivity,
                        "Autenticación biométrica exitosa",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToMainActivity(UserRole.BUYER)
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
}
