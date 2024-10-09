package com.farukaydin.kursapp.view
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class GalleryPermissionManager(
    private val fragment: Fragment,
    private val onPermissionGranted: () -> Unit
) {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    init {
        registerLauncher()
    }

    private fun registerLauncher() {
        permissionLauncher = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                onPermissionGranted()
            } else {
                Toast.makeText(fragment.requireContext(), "İzin verilmedi", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun requestPermission(view: View) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.requireActivity(), permission)) {
                Snackbar.make(view, "Galeriye ulaşıp görsel almamız lazım!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("İzin ver") {
                        permissionLauncher.launch(permission)
                    }.show()
            } else {
                permissionLauncher.launch(permission)
            }
        } else {
            onPermissionGranted()
        }
    }
}