package com.compensar.print3dstore.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.compensar.print3dstore.R
import com.compensar.print3dstore.databinding.ActivityRegisterBinding
import com.compensar.print3dstore.domain.model.UserRole
import com.compensar.print3dstore.presentation.admin.AdminMainActivity
import com.compensar.print3dstore.presentation.buyer.BuyerMainActivity
import com.compensar.print3dstore.presentation.seller.SellerMainActivity

/**
 * Activity para el registro de nuevos usuarios con Firebase Authentication
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViews()
        observeViewModel()
    }
    
    private fun setupViews() {
        // Botón de volver
        binding.btnBack.setOnClickListener {
            finish()
        }
        
        // Botón de registro
        binding.btnRegister.setOnClickListener {
            if (!binding.cbPrivacyPolicy.isChecked) {
                Toast.makeText(
                    this,
                    "Debes aceptar la Política de Privacidad",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            
            registerUser()
        }
    }
    
    private fun observeViewModel() {
        viewModel.registerState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    showLoading(true)
                }
                is AuthState.Success -> {
                    showLoading(false)
                    Toast.makeText(
                        this,
                        "Registro exitoso. Bienvenido a Print3D Store!",
                        Toast.LENGTH_LONG
                    ).show()
                    navigateToMainActivity(state.user.role)
                }
                is AuthState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    // No aplica para registro
                }
            }
        }
    }
    
    private fun registerUser() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val role = getSelectedRole()
        
        // Limpiar errores previos
        binding.tilFullName.error = null
        binding.tilEmail.error = null
        binding.tilPhone.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null
        
        viewModel.register(email, password, confirmPassword, fullName, phone, role)
    }
    
    private fun getSelectedRole(): UserRole {
        return when (binding.rgRole.checkedRadioButtonId) {
            R.id.rbBuyer -> UserRole.BUYER
            R.id.rbSeller -> UserRole.SELLER
            else -> UserRole.BUYER
        }
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
        binding.btnRegister.isEnabled = !isLoading
    }
}
