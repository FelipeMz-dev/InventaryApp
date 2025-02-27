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
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.enums.ReportsType
import com.felipemz.inventaryapp.core.models.RangeDateModel
import com.felipemz.inventaryapp.ui.commons.FilterChipRow
import com.felipemz.inventaryapp.ui.commons.fakeCategoriesRating
import com.felipemz.inventaryapp.ui.commons.fakeIntervals
import com.felipemz.inventaryapp.ui.commons.fakeLabelsRating
import com.felipemz.inventaryapp.ui.commons.fakeProductsRating
import com.felipemz.inventaryapp.ui.home.HomeEvent

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
                    ) {}
                    ReportsType.CATEGORIES_RATING  -> ReportRatingItem(
                        modifier = Modifier.fillMaxWidth(),
                        reportType = it,
                        totalValue = 1000,
                        intervals = fakeCategoriesRating
                    ) {}
                    ReportsType.LABELS_RATING  -> ReportRatingItem(
                        modifier = Modifier.fillMaxWidth(),
                        reportType = it,
                        totalValue = 1000,
                        intervals = fakeLabelsRating
                    ) {}
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