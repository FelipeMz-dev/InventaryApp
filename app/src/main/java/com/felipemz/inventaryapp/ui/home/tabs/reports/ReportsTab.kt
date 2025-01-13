package com.felipemz.inventaryapp.ui.home.tabs.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.entitys.CategoryEntity
import com.felipemz.inventaryapp.core.entitys.CategoryRatingEntity
import com.felipemz.inventaryapp.core.entitys.ProductEntity
import com.felipemz.inventaryapp.core.entitys.ProductRatingEntity
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.enums.ReportsType
import com.felipemz.inventaryapp.core.models.RangeDateModel
import com.felipemz.inventaryapp.ui.commons.FilterChipRow
import com.felipemz.inventaryapp.ui.home.HomeEvent
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage

@Composable
internal fun ReportsTab(
    chipSelected: ReportsFilterDate?,
    customDateSelected: RangeDateModel?,
    eventHandler: (HomeEvent) -> Unit,
) {

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = chipSelected?.getDateRange() ?: customDateSelected?.title ?: String(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
        )

        FilterChipRow(
            modifier = Modifier.fillMaxWidth(),
            text = { it.text },
            chipList = ReportsFilterDate.entries,
            chipSelected = chipSelected,
            onSelectChip = { eventHandler(HomeEvent.OnReportsFilterSelected(it)) }
        )

        Column(modifier = Modifier.fillMaxWidth()) {

            ReportsType.entries.forEach {
                when (it) {
                    ReportsType.PRODUCTS_RATING  -> ReportRatingItem(
                        modifier = Modifier.fillMaxWidth(),
                        reportType = it,
                        totalValue = 1000,
                        intervals = fakeProductsRating
                    )
                    ReportsType.CATEGORIES_RATING  -> ReportRatingItem(
                        modifier = Modifier.fillMaxWidth(),
                        reportType = it,
                        totalValue = 1000,
                        intervals = fakeCategoriesRating
                    )
                    else -> ReportItem(
                        modifier = Modifier.fillMaxWidth(),
                        reportType = it,
                        totalValue = 1000,
                        intervals = fakeIntervals
                    )
                }
            }
        }
    }
}

private val fakeIntervals = listOf(
    Pair("de las 00:00 a las 06:00", 0),
    Pair("de las 06:00 a las 12:00", 100),
    Pair("de las 12:00 a las 18:00", 200),
    Pair("de las 18:00 a las 24:00", 300)
)

private val fakeCategoriesRating = listOf(
    CategoryRatingEntity(
        category = CategoryEntity(
            id = 1,
            name = "Comida",
            color = R.color.red_dark
        ),
        rating = 100
    ),
    CategoryRatingEntity(
        category = CategoryEntity(
            id = 2,
            name = "Bebida",
            color = R.color.blue
        ),
        rating = 98
    ),
    CategoryRatingEntity(
        category = CategoryEntity(
            id = 3,
            name = "Hogar",
            color = R.color.pink
        ),
        rating = 90
    ),
    CategoryRatingEntity(
        category = CategoryEntity(
            id = 4,
            name = "Tecnología",
            color = R.color.teal
        ),
        rating = 87
    ),
    CategoryRatingEntity(
        category = CategoryEntity(
            id = 5,
            name = "Accesorios",
            color = R.color.orange
        ),
        rating = 60
    )
)

private val fakeProductsRating = listOf(
    ProductRatingEntity(
        product = ProductEntity(
            id = 3,
            name = "Delicia mini",
            information = "sin información",
            categoryColor = R.color.red_dark,
            quantity = null,
            price = 9000,
            image = ProductTypeImage.EmojiImage("\uD83C\uDF53")
        ),
        rating = 100,
        totalValue = 10000
    ),
    ProductRatingEntity(
        product = ProductEntity(
            id = 4,
            name = "Coca Cola",
            information = "sin información",
            categoryColor = R.color.blue,
            quantity = 14,
            price = 5000,
            image = ProductTypeImage.EmojiImage("\uD83C\uDF7A")
        ),
        rating = 98,
        totalValue = 14000
    ),
    ProductRatingEntity(
        product = ProductEntity(
            id = 5,
            name = "Shampoo",
            information = "Aquí va toda la información del producto / sin información",
            categoryColor = R.color.pink,
            quantity = 3,
            price = 15000,
            image = ProductTypeImage.EmojiImage("\uD83D\uDC4D")
        ),
        rating = 90,
        totalValue = 20000
    ),
    ProductRatingEntity(
        product = ProductEntity(
            id = 8,
            name = "Audífonos",
            information = "Aquí va toda la información del producto / sin información",
            categoryColor = R.color.teal,
            quantity = 5,
            price = 50000,
            image = ProductTypeImage.PhatImage("")
        ),
        rating = 87,
        totalValue = 20500
    ),
    ProductRatingEntity(
        product = ProductEntity(
            id = 9,
            name = "Cargador",
            information = "Aquí",
            categoryColor = R.color.teal,
            quantity = 6,
            price = 25000,
            image = ProductTypeImage.PhatImage("")
        ),
        rating = 60,
        totalValue = 1200
    )
)

