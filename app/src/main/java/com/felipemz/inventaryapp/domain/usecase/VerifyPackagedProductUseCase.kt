package com.felipemz.inventaryapp.domain.usecase

import com.felipemz.inventaryapp.domain.repository.ProductRepository

class VerifyPackagedProductUseCase(
    private val repository: ProductRepository
){
    suspend operator fun invoke(id: Int): Boolean {
        val packages = repository.countPackagesFromProductId(id)
        return packages > 0
    }
}