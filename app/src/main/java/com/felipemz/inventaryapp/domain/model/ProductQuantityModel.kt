package com.felipemz.inventaryapp.domain.model

import com.felipemz.inventaryapp.core.enums.QuantityType

data class ProductQuantityModel(
    val type: QuantityType = QuantityType.UNIT,
    val quantity: Int = 0,
)