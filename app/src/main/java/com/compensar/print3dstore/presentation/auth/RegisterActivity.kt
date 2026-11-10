package com.compensar.print3dstore.presentation.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.compensar.print3dstore.R
import com.compensar.print3dstore.databinding.ActivityRegisterBinding
import com.compensar.print3dstore.domain.model.User
import com.compensar.print3dstore.domain.model.UserRole

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    // private val auth = FirebaseAuth.getInstance() // Comentado para compilación sin Firebase
    // private val firestore = FirebaseFirestore.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
    }
    
    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        
        binding.btnRegister.setOnClickListener {
            if (binding.cbPrivacyPolicy.isChecked) {
                registerUser()
            } else {
                Toast.makeText(
                    this,
                    "Debes aceptar la Política de Privacidad",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun registerUser() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val role = getSelectedRole()
        
        if (!validateInputs(fullName, email, password)) return
        
        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.btnRegister.isEnabled = false
        
        // Simulación de registro exitoso para demostración
        Toast.makeText(
            this,
            "Registro simulado exitoso. En producción se conectaría a Firebase.",
            Toast.LENGTH_LONG
        ).show()
        
        binding.progressBar.visibility = android.view.View.GONE
        finish()
    }
    
    private fun validateInputs(fullName: String, email: String, password: String): Boolean {
        if (fullName.isEmpty()) {
            binding.tilFullName.error = getString(R.string.error_empty_field)
            return false
        }
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
    
    private fun getSelectedRole(): UserRole {
        return when (binding.rgRole.checkedRadioButtonId) {
            R.id.rbBuyer -> UserRole.BUYER
            R.id.rbSeller -> UserRole.SELLER
            else -> UserRole.BUYER
        }
    }
}
