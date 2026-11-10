package com.compensar.print3dstore.presentation.buyer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.compensar.print3dstore.R
import com.compensar.print3dstore.databinding.ActivityBuyerMainBinding

class BuyerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyerMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
    }
    
    private fun setupNavigation() {
        val navController = findNavController(R.id.navHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
        
        setSupportActionBar(binding.toolbar)
    }
}
