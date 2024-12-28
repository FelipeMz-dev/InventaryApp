package com.felipemz.inventaryapp.core.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

inline fun Boolean.ifTrue(block: () -> Unit) {
    if (this) block()
}

inline fun Boolean.ifFalse(block: () -> Unit) {
    if (!this) block()
}

fun Color.onColor(): Color {
    return if (this.luminance() > 0.4f) Color.Black else Color.White
}