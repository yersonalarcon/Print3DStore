package com.compensar.print3dstore.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper class para manejar la cámara y permisos
 */
class CameraHelper(private val activity: AppCompatActivity) {
    
    private var currentPhotoUri: Uri? = null
    private var onImageCaptured: ((Uri) -> Unit)? = null
    
    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        }
    }
    
    private val takePictureLauncher = activity.registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoUri != null) {
            onImageCaptured?.invoke(currentPhotoUri!!)
        }
    }
    
    /**
     * Verifica si el permiso de cámara está concedido
     */
    fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Solicita permiso de cámara
     */
    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    
    /**
     * Toma una foto
     */
    fun takePicture(onImageCaptured: (Uri) -> Unit) {
        this.onImageCaptured = onImageCaptured
        
        if (hasCameraPermission()) {
            launchCamera()
        } else {
            requestCameraPermission()
        }
    }
    
    /**
     * Lanza la cámara
     */
    private fun launchCamera() {
        try {
            val photoFile = createImageFile()
            currentPhotoUri = FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.fileprovider",
                photoFile
            )
            takePictureLauncher.launch(currentPhotoUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Crea un archivo de imagen temporal
     */
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = activity.cacheDir
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }
}
