package com.felipemz.inventaryapp.core.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import java.time.Clock
import java.time.LocalDate
import java.util.Date

inline fun Boolean.ifTrue(block: () -> Unit) {
    if (this) block()
}

inline fun Boolean.ifFalse(block: () -> Unit) {
    if (!this) block()
}

fun Color.onColor(): Color {
    return if (this.luminance() > 0.4f) Color.Black else Color.White
}

fun Long.toLocalDate(): LocalDate {
    return Date(this).toInstant().atZone(Clock.systemDefaultZone().zone).toLocalDate().plusDays(1)
}

fun LocalDate.toLong(): Long {
    return Date.from(this.atStartOfDay(Clock.systemDefaultZone().zone).toInstant()).time
}

fun Any?.isNull(): Boolean {
    return this == null
}

inline fun <T> T?.ifNotNull(block: (T) -> Unit): Unit? {
    if (this != null) block(this) else return null
    return Unit
}

fun <T> T?.orDefault(default: T): T {
    return this ?: default
}