package com.compensar.print3dstore.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.compensar.print3dstore.R
import com.compensar.print3dstore.databinding.ActivityLoginBinding
import com.compensar.print3dstore.domain.model.UserRole
import com.compensar.print3dstore.presentation.admin.AdminMainActivity
import com.compensar.print3dstore.presentation.buyer.BuyerMainActivity
import com.compensar.print3dstore.presentation.seller.SellerMainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()
    
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
                loginWithEmail(email, password)
            }
        }
        
        binding.btnBiometric.setOnClickListener {
            showBiometricPrompt()
        }
        
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        
        binding.tvForgotPassword.setOnClickListener {
            // TODO: Implementar recuperación de contraseña
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
    
    private fun loginWithEmail(email: String, password: String) {
        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.btnLogin.isEnabled = false
        
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnLogin.isEnabled = true
                
                if (task.isSuccessful) {
                    // TODO: Obtener rol del usuario desde Firestore
                    // Por ahora navegamos según mock
                    navigateToMainActivity(UserRole.BUYER)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.error_login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
                    // TODO: Recuperar credenciales guardadas
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
