package com.felipemz.inventaryapp.domain.repository.mapper

import com.felipemz.inventaryapp.core.EMPTY_STRING
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.data.local.entity.ProductEntity
import com.felipemz.inventaryapp.data.local.entity.ProductPackageEntity
import com.felipemz.inventaryapp.data.local.relations.ProductWithCategoryAndPackages
import com.felipemz.inventaryapp.domain.model.PRODUCT_TYPE_IMAGE_EMOJI
import com.felipemz.inventaryapp.domain.model.PRODUCT_TYPE_IMAGE_LETTER
import com.felipemz.inventaryapp.domain.model.PRODUCT_TYPE_IMAGE_PHAT
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductPackageModel
import com.felipemz.inventaryapp.domain.model.ProductQuantityModel
import com.felipemz.inventaryapp.domain.model.ProductTypeImage

fun ProductEntity.toImage(): ProductTypeImage = when (imageType) {
    PRODUCT_TYPE_IMAGE_EMOJI -> ProductTypeImage.EmojiImage(imageValue)
    PRODUCT_TYPE_IMAGE_PHAT -> ProductTypeImage.PhatImage(imageValue)
    else -> ProductTypeImage.LetterImage(imageValue)
}

fun ProductEntity.toQuantityModel(): ProductQuantityModel? {
    if (quantityType.isNullOrEmpty()) return null
    return quantity?.let {
        ProductQuantityModel(
            type = QuantityType.valueOf(quantityType),
            quantity = it
        )
    }
}

fun ProductWithCategoryAndPackages.toProductModel(): ProductModel {
    return ProductModel(
        id = product.id,
        name = product.name,
        price = product.price.toInt(),
        category = category.toModel(),
        image = product.toImage(),
        cost = product.cost?.toInt(),
        barCode = product.barCode,
        description = product.description,
        quantityModel = product.toQuantityModel(),
        packageProducts = packageProducts.flatMap { packageWithQuantity ->
            packageWithQuantity.packages.map { it.toModel() }
        }.takeIf { packageProducts.isNotEmpty() }
    )
}

fun ProductPackageEntity.toModel(): ProductPackageModel {
    return ProductPackageModel(
        productId = productId,
        quantity = quantity
    )
}

fun ProductModel.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    price = price.toDouble(),
    categoryId = category.id,
    imageType = toImageType(),
    imageValue = toImageValue(),
    cost = cost?.toDouble(),
    barCode = barCode,
    description = description,
    quantityType = quantityModel?.type?.name,
    quantity = quantityModel?.quantity
)

private fun ProductModel.toImageType() = when (image) {
    is ProductTypeImage.EmojiImage -> PRODUCT_TYPE_IMAGE_EMOJI
    is ProductTypeImage.PhatImage -> PRODUCT_TYPE_IMAGE_PHAT
    is ProductTypeImage.LetterImage -> PRODUCT_TYPE_IMAGE_LETTER
}

private fun ProductModel.toImageValue() = when (image) {
    is ProductTypeImage.EmojiImage -> image.emoji
    is ProductTypeImage.PhatImage -> image.path
    is ProductTypeImage.LetterImage -> image.letter
}

fun ProductModel.toPackageEntities(): List<ProductPackageEntity> {
    return packageProducts?.map { it.toEntity(id) } ?: emptyList()
}

fun ProductPackageModel.toEntity(id: Int): ProductPackageEntity {
    return ProductPackageEntity(
        packageId = id,
        productId = productId,
        quantity = quantity
    )
}