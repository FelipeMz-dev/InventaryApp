package com.felipemz.inventaryapp.ui.home.tabs.products

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.onColor
import com.felipemz.inventaryapp.core.utils.CurrencyUtil
import com.felipemz.inventaryapp.domain.model.ProductModel
import com.felipemz.inventaryapp.domain.model.ProductTypeImage
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline

@Composable
internal fun ProductItem(
    modifier: Modifier,
    isSmall: Boolean = false,
    product: ProductModel,
    onClick: (() -> Unit) = {},
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ImageAndCounter(
            image = product.image,
            size = if (isSmall) 40.dp else 48.dp,
            quantity = product.quantityModel?.quantity,
            colorCategory = colorResource(product.category.color)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            NameAndPrice(
                modifier = Modifier.fillMaxWidth(),
                name = product.name,
                price = product.price,
                isSmall = isSmall,
                hasName = product.name.isNotEmpty(),
            )

            InformationField(
                modifier = Modifier.height(IntrinsicSize.Max),
                product = product
            )
        }
    }
}

@Composable
internal fun ProductBillItem(
    modifier: Modifier,
    showTotal: Boolean,
    product: ProductModel,
    quantity: Int,
    onQuantity: (ProductQuantityActionType) -> Unit = {},
    onClick: (() -> Unit) = {},
) {

    val name = product.name.takeIf { it.isNotEmpty() } ?: stringResource(R.string.copy_without_concept)

    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ImageAndCounter(
            image = product.image,
            size = 40.dp,
            quantity = product.quantityModel?.quantity,
            colorCategory = if (product.category.color == 0) MaterialTheme.colorScheme.secondaryContainer
            else colorResource(product.category.color)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            NameAndPrice(
                modifier = Modifier.fillMaxWidth(),
                name = name,
                price = product.price,
                isSmall = true,
                hasName = product.name.isNotEmpty(),
            )

            QuantityField(
                product = product,
                quantity = quantity,
                showTotal = showTotal,
                onQuantity = onQuantity
            )
        }
    }
}

@Composable
private fun QuantityField(
    product: ProductModel,
    quantity: Int,
    showTotal: Boolean,
    onQuantity: (ProductQuantityActionType) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        showTotal.ifTrue {
            Text(
                modifier = Modifier.weight(1f),
                text = "Total: ${CurrencyUtil.formatPrice(product.price.toLong() * quantity)}",
                color = MaterialTheme.colorScheme.outline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }

        product.quantityModel?.type?.text?.let {
            Text(

                modifier = Modifier
                    .weight(0.5f)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                text = "($it)",
                maxLines = 1,
                overflow = TextOverflow.MiddleEllipsis,
                fontSize = 14.sp
            )
        }

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onQuantity(ProductQuantityActionType.SUBTRACT) }
                .padding(4.dp)
                .size(16.dp),
            imageVector = Icons.Default.KeyboardArrowLeft,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = null
        )

        TextButtonUnderline(
            text = "$quantity"
        ) { onQuantity(ProductQuantityActionType.UPDATE) }

        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onQuantity(ProductQuantityActionType.ADD) }
                .padding(4.dp)
                .size(16.dp),
            imageVector = Icons.Default.KeyboardArrowRight,
            tint = MaterialTheme.colorScheme.onPrimary,
            contentDescription = null
        )
    }
}

enum class ProductQuantityActionType {
    ADD, SUBTRACT, UPDATE
}

@Composable
private fun InformationField(
    modifier: Modifier,
    product: ProductModel
) {
    Row(modifier) {

        val textType = product.packageProducts?.let {
            stringResource(R.string.copy_package)
        } ?: product.quantityModel?.type?.text

        Text(
            modifier = Modifier.weight(1f),
            text = product.description.takeUnless {
                it.isNullOrEmpty()
            } ?: stringResource(R.string.copy_without_information),
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        textType?.let { Text(text = "($it)") }
    }
}

@Composable
private fun NameAndPrice(
    modifier: Modifier,
    name: String,
    price: Int,
    isSmall: Boolean,
    hasName: Boolean,
) {
    Row(modifier) {
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = if (isSmall) 16.sp else 18.sp,
            textDecoration = if (hasName) TextDecoration.None else TextDecoration.LineThrough,
            color = MaterialTheme.colorScheme.let {
                if (hasName) it.onSurface else it.outline
            }
        )

        Text(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = CircleShape
                )
                .padding(horizontal = 6.dp),
            text = CurrencyUtil.formatPrice(price),
            fontWeight = FontWeight.Bold,
            fontSize = if (isSmall) 14.sp else 16.sp
        )
    }
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

    val backgroundModifier = Modifier.background(
        color = backgroundColor,
        shape = CircleShape
    )

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
        is ProductTypeImage.EmojiImage -> {
            if (image.emoji.isEmpty()) {
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
        }
        is ProductTypeImage.PhatImage -> {

            var showDefault by remember { mutableStateOf(false) }

            val painter = rememberAsyncImagePainter(
                model = image.path,
                filterQuality = FilterQuality.Low,
                onError = { showDefault = true },
            )

            if (showDefault) EmptyImage(
                modifier = modifier.then(backgroundModifier),
                typeImage = image
            ) else Image(
                modifier = modifier.then(backgroundModifier),
                painter = painter,
                contentDescription = null
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