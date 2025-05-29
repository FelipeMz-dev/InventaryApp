package com.felipemz.inventaryapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.ui.commons.CommonCustomDialog

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun MovementLabelDialog(
    labelList: List<String>,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
) {
    CommonCustomDialog(
        title = stringResource(R.string.copy_select_label),
        onDismiss = { onDismiss() }
    ) {
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            labelList.forEach { label ->
                Text(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .clickable { onSelect(label) }
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    text = label
                )
            }
        }
    }
}