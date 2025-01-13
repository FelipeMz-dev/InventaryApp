package com.felipemz.inventaryapp.ui.home.tabs.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.extensions.onColor
import com.felipemz.inventaryapp.core.utils.PriceUtil

@Composable
internal fun ProductItem(
    modifier: Modifier,
    product: ProductEntity,
) {
    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ImageAndCounter(
            image = product.image,
            quantity = product.quantity,
            colorCategory = colorResource(product.categoryColor)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            NameAndPrice(product)

            Row(modifier = Modifier.height(IntrinsicSize.Max)) {

                Text(
                    text = product.information,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NameAndPrice(product: ProductEntity) = Row {

    Text(
        modifier = Modifier.weight(1f),
        text = product.name,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = 18.sp
    )

    Text(
        modifier = Modifier
            .background(
                color = Color.Green.copy(alpha = 0.4f),
                shape = CircleShape
            )
            .padding(horizontal = 6.dp),
        text = PriceUtil.formatPrice(product.price),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
internal fun ImageAndCounter(
    image: ProductTypeImage,
    quantity: Int?,
    colorCategory: Color,
    size: Dp = 42.dp,
    spaceCounter: Dp = 4.dp,
) = Box(contentAlignment = Alignment.TopEnd) {

    ImageByType(
        modifier = Modifier
            .padding(
                end = spaceCounter,
                bottom = spaceCounter,
                top = spaceCounter
            )
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = colorCategory,
                shape = CircleShape
            )
            .padding(4.dp)
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
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {

    val backgroundModifier by remember {
        derivedStateOf {
            Modifier.background(
                color = backgroundColor.copy(alpha = 0.2f),
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
                text = image.letter,
                fontWeight = FontWeight.Black,
                fontSize = (size.value - 24).sp
            )
        }
        is ProductTypeImage.EmojiImage -> Box(
            modifier = modifier
                .fillMaxSize()
                .then(backgroundModifier)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = image.emoji,
                fontSize = (size.value - 20).sp
            )
        }
        is ProductTypeImage.PhatImage -> Image(
            modifier = modifier.then(backgroundModifier),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null
        )
    }
}

sealed interface ProductTypeImage {
    data class LetterImage(val letter: String) : ProductTypeImage
    data class EmojiImage(val emoji: String) : ProductTypeImage
    data class PhatImage(val path: String) : ProductTypeImage
}

@Preview
@Composable
private fun Preview() {
    ProductItem(
        modifier = Modifier,
        product = ProductEntity(
            name = "Fresa delicia",
            information = "Aquí va toda la información del producto / sin información",
            categoryColor = R.color.red_dark,
            quantity = 8,
            price = 999900000,
            image = ProductTypeImage.PhatImage("")
            //image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
        )
    )
}