package com.felipemz.inventaryapp.core.extensions

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.felipemz.inventaryapp.core.utils.BitmapUtil.setAspectRatio
import java.io.File

fun Context.hasCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.getBitmapFromUri(uri: Uri): Bitmap {
    val inputStream = contentResolver.openInputStream(uri)
    return inputStream.use { stream ->
        BitmapFactory.decodeStream(stream)
    }
}

fun Context.createImageUri(): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}

fun Context.saveBitmapToUri(bitmap: Bitmap): Uri {
    val file = File(cacheDir, "${System.currentTimeMillis()}.jpg")
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return Uri.fromFile(file)
}