package com.example.converter.mvp.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import io.reactivex.rxjava3.core.Completable
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ConvertAndSaveImpl(private val currentContext: Context) : IConvertAndSave {

    override fun convertToPngRx(uri: String?) = Completable.create { emmiter ->
        uri?.toUri()?.let {
            val externalStorageState = Environment.getExternalStorageState()
            if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
                val storageDirectory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File(storageDirectory, "${System.currentTimeMillis()}.png")
                try {
                    Thread.sleep(3000)
                    val stream: OutputStream = FileOutputStream(file)
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(currentContext.contentResolver, it)
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(currentContext.contentResolver, it)
                    }
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    stream.flush()
                    stream.close()
                    Log.d("@@@", Thread.currentThread().name)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                emmiter.onComplete()
            }
        }
    }
}