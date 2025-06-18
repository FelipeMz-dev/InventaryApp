package com.felipemz.inventaryapp.ui.home.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.ui.home.tabs.HomeTabs

@Composable
fun FABHome(
    tabSelected: HomeTabs,
    onClick: () -> Unit,
) {
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