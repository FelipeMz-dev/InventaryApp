package com.felipemz.inventaryapp.core.entitys

import com.felipemz.inventaryapp.core.enums.PackageType
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

data class ProductEntity(
    val id: Int = 0,
    val name: String = String(),
    val description: String = String(),
    val categoryColor: Int = 0,
    val price: Int = 0,
    val image: ProductTypeImage = ProductTypeImage.LetterImage(String()),
    val category: CategoryEntity = CategoryEntity(),
    val quantityType: QuantityType? = null,
    val quantity: Int? = null,
    val packageType: PackageProductModel? = null,
)

data class ProductPackEntity(
    val id: Int = 0,
    val name: String = String(),
    val price: Int = 0,
    val productQuantity: Int = 0,
    val quantity: Int = 0,
    val quantityType: QuantityType? = null,
)

data class ProductSelectionEntity(
    val id: Int = 0,
    val quantity: Int = 0,
)

data class ProductQuantityEntity(
    val product: ProductEntity? = null,
    val quantity: Int = 0,
)

fun ProductEntity.toProductPackEntity(quantity: Int): ProductPackEntity = ProductPackEntity(
    id = this.id,
    name = this.name,
    price = this.price,
    quantity = quantity,
    quantityType = this.quantityType ?: QuantityType.UNIT
)

fun ProductEntity.toProductSelectedEntity(quantity: Int): ProductSelectionEntity = ProductSelectionEntity(
    id = this.id,
    quantity = quantity
)

fun ProductPackEntity.toProductSelectedEntity(): ProductSelectionEntity = ProductSelectionEntity(
    id = this.id,
    quantity = this.quantity
)

sealed interface PackageProductModel {
    data class Package(val product: ProductPackEntity? = null): PackageProductModel
    data class Pack(val products: List<ProductPackEntity> = emptyList()) : PackageProductModel

    fun getPackageType(): PackageType = when (this) {
        is Package -> PackageType.PACKAGE
        is Pack -> PackageType.PACk
    }

    fun isFull(): Boolean = when (this) {
        is Package -> this.product != null
        is Pack -> this.products.isNotEmpty()
    }
}