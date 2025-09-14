package com.felipemz.inventaryapp.domain.model

import com.felipemz.inventaryapp.core.EMPTY_STRING

const val PRODUCT_TYPE_IMAGE_LETTER = "LetterImage"
const val PRODUCT_TYPE_IMAGE_EMOJI = "EmojiImage"
const val PRODUCT_TYPE_IMAGE_PHAT = "PhatImage"

sealed interface ProductTypeImage {
    data class LetterImage(val letter: String) : ProductTypeImage
    data class EmojiImage(val emoji: String) : ProductTypeImage
    data class PhatImage(val path: String) : ProductTypeImage

    companion object {
        val emptyTypes: List<ProductTypeImage>
            get() = listOf(
                LetterImage(EMPTY_STRING),
                EmojiImage(EMPTY_STRING),
                PhatImage(EMPTY_STRING)
            )
    }

    fun ifNotEmptyOrDefault(default: ProductTypeImage) = default.takeIf {
        when (this) {
            is LetterImage -> letter.isEmpty()
            is EmojiImage -> emoji.isEmpty()
            is PhatImage -> path.isEmpty()
        }
    } ?: this
}