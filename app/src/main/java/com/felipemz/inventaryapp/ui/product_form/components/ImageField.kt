package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.model.CategoryEntity
import com.felipemz.inventaryapp.ui.home.tabs.products.ImageAndCounter
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

@Composable
internal fun ImageField(
    modifier: Modifier,
    images: List<ProductTypeImage>,
    imageSelected: ProductTypeImage,
    category: CategoryEntity?,
    onOpen: suspend () -> Unit,
    onSelect: (ProductTypeImage) -> Unit,
) {
    CommonFormField(
        modifier = modifier,
        title = stringResource(R.string.copy_image_dots),
        concealable = true,
        isMandatory = false,
        visible = false,
        onOpen = onOpen
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            images.forEach { image ->
                ImageAndCounter(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onSelect(image) }
                        .alpha(if (imageSelected::class == image::class) 1f else 0.4f),
                    image = image,
                    quantity = null,
                    spaceCounter = 0.dp,
                    size = 56.dp,
                    colorCategory = category?.let {
                        colorResource(id = it.color)
                    } ?: MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}