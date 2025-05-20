package com.felipemz.inventaryapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val cost: Double = 0.0,
    val description: String = "",
    val imageUrl: String? = null,
    val categoryId: Int? = null,
    val isPackage: Boolean = false,
)