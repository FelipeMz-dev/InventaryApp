package com.felipemz.inventaryapp.core.enums

import androidx.compose.runtime.mutableStateOf
import com.felipemz.inventaryapp.model.ProductModel

enum class PackageType(val text: String, val product: Any) {
    PACKAGE("Paquete", mutableStateOf(ProductModel())),
    PACk("COMBO", Any()),
}