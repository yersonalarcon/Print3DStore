package com.compensar.print3dstore.presentation.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.compensar.print3dstore.R
import com.compensar.print3dstore.databinding.ActivityAdminMainBinding

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
    }
    
    private fun setupNavigation() {
        val navController = findNavController(R.id.navHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
        
        setSupportActionBar(binding.toolbar)
    }
}
