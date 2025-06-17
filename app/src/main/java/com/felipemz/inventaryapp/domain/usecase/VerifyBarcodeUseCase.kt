package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.repository.ProductRepository

class VerifyBarcodeUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(barcode: String): Boolean {
        return productRepository.verifyBarcode(barcode)
    }
}