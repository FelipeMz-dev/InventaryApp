package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.repository.ProductRepository

class GetProductFromBarcodeUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(barcode: String) = repository.getFromBarcode(barcode)
}