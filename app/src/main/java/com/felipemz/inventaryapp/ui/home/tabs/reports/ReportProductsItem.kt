package com.felipemz.inventaryapp.ui.home.tabs.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.domain.model.BaseRatingModel
import com.felipemz.inventaryapp.domain.model.CategoryRatingModel
import com.felipemz.inventaryapp.domain.model.LabelRatingModel
import com.felipemz.inventaryapp.domain.model.ProductRatingModel
import com.felipemz.inventaryapp.core.enums.ReportsType
import com.felipemz.inventaryapp.core.utils.CurrencyUtil
import com.felipemz.inventaryapp.ui.commons.HorizontalDotDivider
import com.felipemz.inventaryapp.ui.commons.TextButtonUnderline
import com.felipemz.inventaryapp.ui.home.tabs.products.ImageAndCounter

@Composable
internal fun ReportRatingItem(
    modifier: Modifier,
    reportType: ReportsType,
    totalValue: Int,
    intervals: List<BaseRatingModel>,
    onSeeMore: () -> Unit
) = Column(
    modifier = modifier.padding(
        horizontal = 12.dp,
        vertical = 8.dp
    )
) {

    HeaderReportsItem(
        modifier = Modifier.fillMaxWidth(),
        reportType = reportType,
        totalValue = totalValue
    )

    intervals.forEach {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .align(Alignment.End)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            when (it) {
                is ProductRatingModel -> ImageAndCounter(
                    image = it.product.image,
                    quantity = null,
                    colorCategory = colorResource(id = it.product.category.color),
                    size = 32.dp,
                )
                is CategoryRatingModel -> Spacer(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            color = colorResource(id = it.category.color),
                            shape = CircleShape
                        )
                        .size(24.dp)
                )
                is LabelRatingModel -> Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.Start)
                        .padding(vertical = 2.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .padding(horizontal = 6.dp),
                    text = it.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                else -> Unit
            }

            if (it !is LabelRatingModel) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = when (it) {
                        is ProductRatingModel -> it.product.name
                        is CategoryRatingModel -> it.category.name
                        else -> String()
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }

            Text(
                text = "(${it.rating})",
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                text = CurrencyUtil.formatPrice(it.totalValue),
            )
        }
    }

    TextButtonUnderline(
        modifier = Modifier.fillMaxWidth(),
        text = "Ver más",
        isEnabled = true
    ) { onSeeMore() }

    HorizontalDotDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    )
}