package com.compensar.print3dstore.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.compensar.print3dstore.R
import com.compensar.print3dstore.presentation.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    
    private val auth = FirebaseAuth.getInstance()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthAndNavigate()
        }, 2000)
    }
    
    private fun checkAuthAndNavigate() {
        val currentUser = auth.currentUser
        
        val intent = if (currentUser != null) {
            // Usuario ya autenticado, navegar a su activity principal
            // TODO: Obtener rol y navegar según corresponda
            Intent(this, LoginActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        
        startActivity(intent)
        finish()
    }
}
