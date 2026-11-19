package com.compensar.print3dstore.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

/**
 * Helper class para manejar la geolocalización y permisos
 */
class LocationHelper(private val activity: AppCompatActivity) {
    
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)
    
    private var onLocationReceived: ((Location) -> Unit)? = null
    private var onLocationError: ((String) -> Unit)? = null
    
    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            getCurrentLocation()
        } else {
            onLocationError?.invoke("Permiso de ubicación denegado")
        }
    }
    
    /**
     * Verifica si los permisos de ubicación están concedidos
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Solicita permisos de ubicación
     */
    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
    
    /**
     * Obtiene la ubicación actual
     */
    fun getLocation(
        onSuccess: (Location) -> Unit,
        onError: (String) -> Unit
    ) {
        this.onLocationReceived = onSuccess
        this.onLocationError = onError
        
        if (hasLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }
    
    /**
     * Obtiene la ubicación actual del dispositivo
     */
    private fun getCurrentLocation() {
        try {
            if (!hasLocationPermission()) {
                onLocationError?.invoke("Permiso de ubicación denegado")
                return
            }
            
            val cancellationTokenSource = CancellationTokenSource()
            
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    onLocationReceived?.invoke(location)
                } else {
                    onLocationError?.invoke("No se pudo obtener la ubicación")
                }
            }.addOnFailureListener { exception ->
                onLocationError?.invoke(exception.message ?: "Error al obtener ubicación")
            }
        } catch (e: SecurityException) {
            onLocationError?.invoke("Error de seguridad al obtener ubicación")
        }
    }
    
    /**
     * Formatea una ubicación como string
     */
    fun formatLocation(location: Location): String {
        return "Lat: ${String.format("%.6f", location.latitude)}, " +
               "Lng: ${String.format("%.6f", location.longitude)}"
    }
}
