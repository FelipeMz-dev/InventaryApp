package com.felipemz.inventaryapp.ui.home.tabs.products

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.onColor
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import com.felipemz.inventaryapp.core.utils.PriceUtil
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductTypeImage
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import java.io.File

@Composable
internal fun ProductItem(
    modifier: Modifier,
    isSmall: Boolean = false,
    product: ProductModel,
    selection: Int? = null,
    onQuantity: (() -> Unit)? = null,
    onSelectionChange: ((Int) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .clickable { onClick?.invoke() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ImageAndCounter(
            image = product.image,
            size = if (isSmall) 40.dp else 48.dp,
            quantity = product.quantityModel?.quantity,
            colorCategory = if (product.category.color == 0) MaterialTheme.colorScheme.secondaryContainer
            else colorResource(product.category.color)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            NameAndPrice(
                name = product.name,
                price = product.price,
                isSmall = isSmall
            )

            selection?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Total: ${PriceUtil.formatPrice(product.price * it)}",
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = if (isSmall) 14.sp else 16.sp
                    )

                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                onSelectionChange?.invoke(it - 1)
                            }
                            .padding(4.dp)
                            .size(16.dp),
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null
                    )

                    TextButtonUnderline(
                        text = "$it${product.quantityModel?.type?.initial?.let { "/$it" }.orEmpty()}"
                    ) { onQuantity?.invoke() }

                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                onSelectionChange?.invoke(it + 1)
                            }
                            .padding(4.dp)
                            .size(16.dp),
                        imageVector = Icons.Default.KeyboardArrowRight,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null
                    )
                }
            } ?: Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                Text(
                    text = product.description.takeUnless {
                        it.isEmpty()
                    } ?: stringResource(R.string.copy_without_information),
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NameAndPrice(
    name: String,
    price: Int,
    isSmall: Boolean,
) = Row {

    Text(
        modifier = Modifier.weight(1f),
        text = name,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = if (isSmall) 16.sp else 18.sp
    )

    Text(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = CircleShape
            )
            .padding(horizontal = 6.dp),
        text = PriceUtil.formatPrice(price),
        fontWeight = FontWeight.Bold,
        fontSize = if (isSmall) 14.sp else 16.sp
    )
}

@Composable
internal fun ImageAndCounter(
    modifier: Modifier = Modifier,
    image: ProductTypeImage,
    quantity: Int?,
    colorCategory: Color,
    size: Dp = 48.dp,
    spaceCounter: Dp = 4.dp,
) = Box(contentAlignment = Alignment.TopEnd) {

    ImageByType(
        modifier = modifier
            .padding(
                end = spaceCounter,
                bottom = spaceCounter,
                top = spaceCounter
            )
            .border(
                width = 2.5.dp,
                color = colorCategory,
                shape = CircleShape
            )
            .padding(4.5.dp)
            .clip(CircleShape)
            .size(size),
        image = image,
        size = size,
    )

    quantity?.let {
        Text(
            modifier = Modifier
                .background(
                    color = colorCategory,
                    shape = CircleShape
                )
                .padding(horizontal = 5.dp),
            text = it.toString(),
            color = colorCategory.onColor(),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun ImageByType(
    modifier: Modifier,
    image: ProductTypeImage,
    size: Dp,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer
) {

    val backgroundModifier by remember {
        derivedStateOf {
            Modifier.background(
                color = backgroundColor,
                shape = CircleShape
            )
        }
    }

    when (image) {
        is ProductTypeImage.LetterImage -> Box(
            modifier = modifier
                .fillMaxSize()
                .then(backgroundModifier)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = image.letter.ifEmpty { "⚠" },
                fontWeight = FontWeight.Black,
                fontSize = (size.value - 24).sp
            )
        }
        is ProductTypeImage.EmojiImage -> if (image.emoji.isEmpty()) {
            EmptyImage(
                modifier = modifier.then(backgroundModifier),
                typeImage = image
            )
        } else Box(
            modifier = modifier
                .fillMaxSize()
                .then(backgroundModifier)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = image.emoji,
                fontSize = (size.value - 18).sp
            )
        }
        is ProductTypeImage.PhatImage -> {

            val bitmap by remember(image.path) {
                derivedStateOf {
                    tryOrDefault(null) {
                        val inputStream = File(image.path).inputStream()
                        inputStream.use { BitmapFactory.decodeStream(it) }
                    }
                }
            }

            bitmap?.let {
                Image(
                    modifier = modifier.then(backgroundModifier),
                    bitmap = it.asImageBitmap(),
                    contentDescription = null
                )
            } ?: EmptyImage(
                modifier = modifier.then(backgroundModifier),
                typeImage = image
            )
        }
    }
}

@Composable
private fun EmptyImage(
    modifier: Modifier,
    typeImage: ProductTypeImage
) = Icon(
    modifier = modifier.padding(if (typeImage is ProductTypeImage.PhatImage) 8.dp else 4.dp),
    imageVector = when (typeImage) {
        is ProductTypeImage.EmojiImage -> ImageVector.vectorResource(id = R.drawable.ic_emoji_empty)
        else -> ImageVector.vectorResource(id = R.drawable.ic_image_empty)
    },
    tint = MaterialTheme.colorScheme.primary,
    contentDescription = null
)