package com.felipemz.inventaryapp.ui.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.HomeTabs
import com.felipemz.inventaryapp.ui.home.HomeEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBarHome(
    tabSelected: HomeTabs,
    isMovementsInverted: Boolean,
    isProductOrderInverted: Boolean,
    eventHandler: (HomeEvent) -> Unit,
) {

    val productsIcon = remember(isProductOrderInverted) {
        derivedStateOf {
            if (isProductOrderInverted) R.drawable.ic_sort_up else R.drawable.ic_sort_down
        }
    }

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        actions = {

            when (tabSelected) {
                HomeTabs.PRODUCTS -> IconButton(onClick = { eventHandler(HomeEvent.OnOpenProductOrderPopup) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = productsIcon.value),
                        contentDescription = null
                    )
                }
                HomeTabs.MOVEMENTS -> IconButton(
                    { eventHandler(HomeEvent.OnMovementsInverted(!isMovementsInverted)) }
                ) {
                    Icon(
                        modifier = Modifier.graphicsLayer(if (isMovementsInverted) -1f else 1f),
                        imageVector = ImageVector.vectorResource(id = R.drawable.sort_vertical_svgrepo_com),
                        contentDescription = null
                    )
                }
                HomeTabs.REPORTS -> IconButton(onClick = { eventHandler(HomeEvent.OpenReportsCalendarPopup) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_calendary),
                        contentDescription = null
                    )
                }
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }
        }
    )
}