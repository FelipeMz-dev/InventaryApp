package com.felipemz.inventaryapp.domain.repository.mapper

import com.felipemz.inventaryapp.core.EMPTY_STRING
import com.felipemz.inventaryapp.data.local.entity.ProductEntity
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

fun ProductEntity.toDomain() = ProductModel(
    id = id,
    name = name,
    price = price.toInt(), //TODO: change to double
    cost = cost.toInt(),    //TODO: change to double
    description = description,
    image = ProductTypeImage.LetterImage(EMPTY_STRING), //TODO: change to imageUri
    category = CategoryModel(), //TODO: change to category
    packageProduct = null, //TODO: change to packageProduct
)

fun ProductModel.toEntity() = ProductEntity(
    id = id,
    name = name,
    price = price.toDouble(), //TODO: change to double
    cost = cost.toDouble(),   //TODO: change to double
    description = description,
    imageUrl = null, //TODO: change to imageUri
    categoryId = category.id,
    isPackage = false, //TODO: change to isPackage
)