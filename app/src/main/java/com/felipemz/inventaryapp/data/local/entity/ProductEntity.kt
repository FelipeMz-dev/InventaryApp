package com.felipemz.inventaryapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.felipemz.inventaryapp.core.EMPTY_STRING
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.domain.model.PRODUCT_TYPE_IMAGE_LETTER

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_DEFAULT
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = EMPTY_STRING,
    val price: Double = 0.0,
    val cost: Double = 0.0,
    val description: String = EMPTY_STRING,
    val categoryId: Int = 0,
    val imageType: String = PRODUCT_TYPE_IMAGE_LETTER,
    val imageValue: String = EMPTY_STRING,
    val quantityType: String = QuantityType.UNIT.name,
    val quantity: Int = 0
)