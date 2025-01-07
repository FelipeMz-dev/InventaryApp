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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.ReportsFilterDate
import com.felipemz.inventaryapp.core.enums.ReportsType
import com.felipemz.inventaryapp.core.models.RangeDateModel
import com.felipemz.inventaryapp.core.utils.FormatDateUtil
import com.felipemz.inventaryapp.ui.commons.FilterChipRow
import com.felipemz.inventaryapp.ui.home.HomeEvent
import java.time.LocalDate

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

            ReportsType.entries.forEach{
                ReportItem(
                    modifier = Modifier.fillMaxWidth(),
                    reportType = it,
                    totalValue = 1000,
                    intervals = fakeIntervals
                )
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

