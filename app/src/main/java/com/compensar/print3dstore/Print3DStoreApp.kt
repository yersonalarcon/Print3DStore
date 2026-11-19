package com.compensar.print3dstore

import android.app.Application
import com.compensar.print3dstore.data.firebase.FirebaseManager

class Print3DStoreApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar Firebase
        FirebaseManager.initialize(this)
    }
}
