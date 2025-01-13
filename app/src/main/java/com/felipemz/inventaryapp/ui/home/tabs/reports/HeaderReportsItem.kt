package com.felipemz.inventaryapp.ui.home.tabs.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.enums.ReportsType
import com.felipemz.inventaryapp.core.utils.PriceUtil

@Composable
internal fun HeaderReportsItem(
    modifier: Modifier,
    reportType: ReportsType,
    totalValue: Int,
) = Row(
    modifier = modifier.padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .padding(6.dp)
                .size(20.dp),
            imageVector = ImageVector.vectorResource(reportType.icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface
        )

        Text(
            text = reportType.text,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }

    Text(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                shape = CircleShape
            )
            .padding(horizontal = 8.dp),
        text = PriceUtil.formatPrice(totalValue),
        fontWeight = FontWeight.Bold,
    )
}