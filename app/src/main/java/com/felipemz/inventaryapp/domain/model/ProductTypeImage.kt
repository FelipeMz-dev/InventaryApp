package com.felipemz.inventaryapp.domain.model

const val PRODUCT_TYPE_IMAGE_LETTER = "LetterImage"
const val PRODUCT_TYPE_IMAGE_EMOJI = "EmojiImage"
const val PRODUCT_TYPE_IMAGE_PHAT = "PhatImage"

sealed interface ProductTypeImage {
    data class LetterImage(val letter: String) : ProductTypeImage
    data class EmojiImage(val emoji: String) : ProductTypeImage
    data class PhatImage(val path: String) : ProductTypeImage

    fun ifNotEmptyOrDefault(default: ProductTypeImage) = default.takeIf {
        when (this) {
            is LetterImage -> letter.isEmpty()
            is EmojiImage -> emoji.isEmpty()
            is PhatImage -> path.isEmpty()
        }
    } ?: this
}