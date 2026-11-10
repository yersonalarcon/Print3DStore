package com.compensar.print3dstore.presentation.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.compensar.print3dstore.R
import com.compensar.print3dstore.databinding.ActivityRegisterBinding
import com.compensar.print3dstore.domain.model.User
import com.compensar.print3dstore.domain.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
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
        
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    saveUserToFirestore(userId, fullName, email, role)
                } else {
                    binding.progressBar.visibility = android.view.View.GONE
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(
                        this,
                        getString(R.string.error_register_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
    
    private fun saveUserToFirestore(
        userId: String,
        fullName: String,
        email: String,
        role: UserRole
    ) {
        val user = User(
            uid = userId,
            email = email,
            fullName = fullName,
            role = role,
            isActive = true,
            createdAt = System.currentTimeMillis()
        )
        
        firestore.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                binding.progressBar.visibility = android.view.View.GONE
                Toast.makeText(
                    this,
                    "Registro exitoso. Inicia sesión para continuar.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            .addOnFailureListener {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnRegister.isEnabled = true
                Toast.makeText(
                    this,
                    "Error al guardar usuario: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
