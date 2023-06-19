package com.example.converter.mvp.model

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import io.reactivex.rxjava3.core.Completable

class ImagePickerImpl(
    registry: ActivityResultRegistry,
    callbackPermission: (granted: Boolean) -> Unit,
    callbackUri: (imageUri: Uri?) -> Unit
) : IImagePicker {

    private var getContent: ActivityResultLauncher<String> =
        registry.register(RESULT_REGISTRY_KEY, ActivityResultContracts.GetContent(), callbackUri)

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registry.register(
            PERMISSION_REGISTRY_KEY,
            ActivityResultContracts.RequestPermission(),
            callbackPermission
        )

    override fun pickImageRx() = Completable.create {
        Log.d("@@@", Thread.currentThread().name)
        getContent.launch("image/*")
    }

    override fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private companion object {
        const val RESULT_REGISTRY_KEY = "pick_image"
        const val PERMISSION_REGISTRY_KEY = "permission"
    }
}