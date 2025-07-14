package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.repository.ProductRepository

class VerifyPackagedProductUseCase(
    private val repository: ProductRepository
){
    suspend operator fun invoke(id: Int): List<ProductModel> {
        return repository.getPackagesIdFromProduct(id)
    }
}