package com.felipemz.inventaryapp.domain.repository.mapper

import com.felipemz.inventaryapp.data.local.entity.CategoryEntity
import com.felipemz.inventaryapp.domain.model.CategoryModel

fun CategoryEntity.toModel(): CategoryModel = CategoryModel(
    id = id,
    name = name,
    position = position,
    color = color
)

fun CategoryModel.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    position = position,
    color = color
)