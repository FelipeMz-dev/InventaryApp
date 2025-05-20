package com.felipemz.inventaryapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.felipemz.inventaryapp.core.enums.MovementItemType

@Entity(tableName = "movements")
data class MovementItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String = MovementItemType.MOVEMENT_PENDING.name,
    val number: Int? = null,
    val date: String = "",
    val time: String = "",
    val amount: Int = 0,
    val labels: String = ""
)