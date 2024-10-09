package com.farukaydin.kursapp.view



import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

//Bu sınıf, galeriye gidip bir görsel seçmeyi ve sonucu yönetmeyi sağlar.
class GalleryImagePicker(
    private val fragment: Fragment,
    private val onImageSelected: (Bitmap) -> Unit
) {
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    init {
        registerLauncher()
    }

    private fun registerLauncher() {
        activityResultLauncher = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intentFromResult = result.data
                val selectedImageUri = intentFromResult?.data
                selectedImageUri?.let { uri ->
                    try {
                        val bitmap = if (Build.VERSION.SDK_INT >= 28) {
                            val source = ImageDecoder.createSource(fragment.requireActivity().contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            MediaStore.Images.Media.getBitmap(fragment.requireActivity().contentResolver, uri)
                        }
                        onImageSelected(bitmap)
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                    }
                }
            }
        }
    }

    fun selectImageFromGallery() {
        val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intentToGallery)
    }
}