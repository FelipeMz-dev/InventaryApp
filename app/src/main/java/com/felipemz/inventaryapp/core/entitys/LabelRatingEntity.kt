package com.felipemz.inventaryapp.core.entitys

data class LabelRatingEntity(
    override val rating: Int = 0,
    override val totalValue: Int = 0,
    val label: String = String(),
) : BaseRatingEntity()