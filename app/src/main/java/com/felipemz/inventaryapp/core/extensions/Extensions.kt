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

fun Any?.isNotNull(): Boolean {
    return this != null
}

inline fun <T> List<T>.ifNotEmpty(block: (List<T>) -> Unit) {
    if (this.isNotEmpty()) block(this)
}

inline fun <T> T?.ifNotNull(block: (T) -> Unit): Unit? {
    if (this != null) block(this) else return null
    return Unit
}

inline fun <T> T?.ifNull(block: () -> Unit): Unit? {
    if (this == null) block() else return null
    return Unit
}

fun <T> T?.orDefault(default: T): T {
    return this ?: default
}

fun <T> tryOrDefault(
    default: T,
    block: () -> T
): T {
    return try {
        block()
    } catch (e: Exception) {
        println("Error occurred: ${e.message}")
        default
    }
}

fun Int.toIdString(): String {
    return try {
        this.toString().padStart(6, '0')
    } catch (e: Exception) {
        println("Error converting Int to String: ${e.message}")
        "000000"
    }
}