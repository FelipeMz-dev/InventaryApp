package com.felipemz.inventaryapp.core.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset

object BitmapUtil {
    fun setAspectRatio(
        bitmap: Bitmap,
        aspectRatio: Float,
    ): Bitmap {
        val width = if (bitmap.width > bitmap.height) {
            (bitmap.height * aspectRatio).toInt()
        } else bitmap.width
        val height = if (bitmap.height > bitmap.width) {
            (bitmap.width * aspectRatio).toInt()
        } else bitmap.height

        return Bitmap.createBitmap(bitmap, 0, 0, width, height)
    }

    fun rotateBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix().apply { postRotate(90f) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun scaleBitmap(
        bitmap: Bitmap,
        scale: Float
    ): Bitmap {
        val safeScale = scale.coerceIn(0.1f, 1f)
        val width = (bitmap.width * safeScale).toInt()
        val height = (bitmap.height * safeScale).toInt()
        return Bitmap.createBitmap(bitmap, 0, 0, width, height)
    }

    fun scaleBitmapFromOffset(
        bitmap: Bitmap,
        scale: Float,
        aspectRatio: Float = if (bitmap.width > bitmap.height) {
            bitmap.height.toFloat() / bitmap.width
        } else bitmap.width.toFloat() / bitmap.height,
        offset: IntOffset = IntOffset.Zero,
        changeOffset: (IntOffset) -> Unit = {},
    ): Bitmap {
        val safeScale = scale.coerceIn(0.1f, 1f)
        val newBitmap = setAspectRatio(bitmap, aspectRatio)
        val width = (newBitmap.width * safeScale).toInt()
        val height = (newBitmap.height * safeScale).toInt()
        val safeOffset = IntOffset(
            x = offset.x.coerceIn(0, newBitmap.width - width),
            y = offset.y.coerceIn(0, newBitmap.height - height)
        )
        changeOffset(safeOffset)
        return Bitmap.createBitmap(newBitmap, safeOffset.x, safeOffset.y, width, height)
    }

    fun dragBitmap(
        bitmap: Bitmap,
        parentBitmap: Bitmap,
        offset: IntOffset,
        changeOffset: (IntOffset) -> Unit,
    ): Bitmap {
        val safeOffset = IntOffset(
            x = offset.x.coerceIn(0, parentBitmap.width - bitmap.width),
            y = offset.y.coerceIn(0, parentBitmap.height - bitmap.height)
        )
        changeOffset(safeOffset)
        return Bitmap.createBitmap(parentBitmap, safeOffset.x, safeOffset.y, bitmap.width, bitmap.height)
    }
}