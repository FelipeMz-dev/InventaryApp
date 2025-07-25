package com.felipemz.inventaryapp.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.ui.home.tabs.HomeTabs

@Composable
fun FABHome(
    tabSelected: HomeTabs,
    onClick: () -> Unit,
    onClickSmall: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SmallFloatingActionButton(
            onClick = onClickSmall,
            containerColor = colorScheme.onPrimaryContainer,
            contentColor = colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    when (tabSelected) {
                        HomeTabs.PRODUCTS -> R.drawable.ic_scanner
                        else -> R.drawable.ic_expenses
                    }
                ),
                contentDescription = null
            )
        }

        FloatingActionButton(onClick = onClick) {
            Icon(
                imageVector = ImageVector.Companion.vectorResource(
                    when (tabSelected) {
                        HomeTabs.PRODUCTS -> R.drawable.ic_product_add
                        else -> R.drawable.ic_register_add
                    }
                ),
                contentDescription = null
            )
        }
    }
}