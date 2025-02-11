package com.felipemz.inventaryapp.core.enums

import androidx.compose.runtime.mutableStateOf
import com.felipemz.inventaryapp.core.entitys.ProductEntity

enum class PackageType(val text: String, val product: Any) {
    PACKAGE("Paquete", mutableStateOf(ProductEntity())),
    PACk("COMBO", Any()),
}