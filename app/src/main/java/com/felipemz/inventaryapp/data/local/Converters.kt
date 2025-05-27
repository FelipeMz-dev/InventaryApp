package com.felipemz.inventaryapp.data.local

import androidx.room.TypeConverter
import com.felipemz.inventaryapp.core.enums.MovementItemType
import com.felipemz.inventaryapp.core.enums.QuantityType

class Converters {

    @TypeConverter
    fun toQuantityType(value: String) = QuantityType.valueOf(value)

    @TypeConverter
    fun fromQuantityType(type: QuantityType) = type.name

    @TypeConverter
    fun fromMovementItemType(value: MovementItemType): String = value.name

    @TypeConverter
    fun toMovementItemType(value: String): MovementItemType = MovementItemType.valueOf(value)

    @TypeConverter
    fun fromStringList(list: List<String>): String = list.joinToString(",")

    @TypeConverter
    fun toStringList(data: String): List<String> = data.split(",").filter { it.isNotBlank() }
}