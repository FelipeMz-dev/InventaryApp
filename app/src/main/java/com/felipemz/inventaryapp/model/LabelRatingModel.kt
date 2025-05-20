package com.felipemz.inventaryapp.model

data class LabelRatingModel(
    override val rating: Int = 0,
    override val totalValue: Int = 0,
    val label: String = String(),
) : BaseRatingModel()