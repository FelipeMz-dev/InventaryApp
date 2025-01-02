package com.felipemz.inventaryapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.HomeTabs
import com.felipemz.inventaryapp.core.extensions.ifFalse
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.ui.commons.PopupDialog
import com.felipemz.inventaryapp.ui.home.tabs.products.InventoryTab
import com.felipemz.inventaryapp.ui.home.tabs.movements.MovementsTab
import com.felipemz.inventaryapp.ui.home.tabs.reports.ReportsTab

@Composable
internal fun HomeScreen(
    state: HomeState,
    eventHandler: (HomeEvent) -> Unit,
) {

    val tabSelected = remember { mutableStateOf(HomeTabs.MOVEMENTS) }
    val pagerState = rememberPagerState(1) { HomeTabs.entries.size }
    val isSearchFocused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val isReports by remember(tabSelected.value) {
        derivedStateOf { tabSelected.value == HomeTabs.REPORTS }
    }

    LaunchedEffect(tabSelected.value) {
        if (pagerState.isScrollInProgress) {
            tabSelected.value = HomeTabs.entries[pagerState.currentPage]
        } else {
            val tab = tabSelected.value.ordinal
            if (pagerState.currentPage != tab) pagerState.animateScrollToPage(tab)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.targetPage == pagerState.currentPage) {
            focusManager.clearFocus()
            tabSelected.value = HomeTabs.entries[pagerState.currentPage]
        }
    }

    Surface {

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            topBar = {
                AnimatedVisibility(!isSearchFocused.value) {
                    TopBarHome(
                        tabSelected = tabSelected.value,
                        isMovementsInverted = state.isMovementsInverted,
                        eventHandler = eventHandler
                    )
                }
            },
            bottomBar = {
                AnimatedVisibility(!isSearchFocused.value) {
                    BottomBarHome(tabSelected)
                }
            },
            floatingActionButton = {
                isReports.ifFalse {
                    FABHome(tabSelected.value) {
                        eventHandler(HomeEvent.OnFAB(tabSelected.value))
                    }
                }
            }
        ) {

            HorizontalPager(
                modifier = Modifier.padding(it),
                state = pagerState,
            ) { page ->
                when (page) {
                    HomeTabs.PRODUCTS.ordinal -> InventoryTab(
                        isInventory = pagerState.settledPage == HomeTabs.PRODUCTS.ordinal,
                        isFocusSearch = isSearchFocused,
                        categories = state.categories,
                        products = state.products
                    )
                    HomeTabs.MOVEMENTS.ordinal -> MovementsTab(
                        movements = state.movements,
                        date = state.currentDate,
                        total = state.totalAmount,
                        chipSelected = state.movementFilterSelected,
                        labelSelected = state.movementLabelSelected,
                        eventHandler = eventHandler
                    )
                    HomeTabs.REPORTS.ordinal -> ReportsTab()
                }
            }
        }

        state.isShowLabelPopup.ifTrue {
            MovementLabelPopup(
                labelList = state.movementLabelList,
                onSelect = { eventHandler(HomeEvent.OnLabelSelected(it)) }
            ) { eventHandler(HomeEvent.OnHideLabelPopup) }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun MovementLabelPopup(
    labelList: List<String>,
    onSelect: (String) -> Unit,
    onClick: () -> Unit,
) {
    PopupDialog(
        title = stringResource(R.string.copy_select_label),
        onClose = { onClick() }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarHome(
    tabSelected: HomeTabs,
    isMovementsInverted: Boolean,
    eventHandler: (HomeEvent) -> Unit,
) {
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
                HomeTabs.PRODUCTS -> IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sort_down),
                        contentDescription = null
                    )
                }
                HomeTabs.MOVEMENTS -> IconButton(
                    { eventHandler(HomeEvent.OnMovementsInverted(!isMovementsInverted)) }
                ) {
                    Icon(
                        modifier = Modifier.graphicsLayer(if (isMovementsInverted) -1f else 1f),
                        painter = painterResource(id = R.drawable.sort_vertical_svgrepo_com),
                        contentDescription = null
                    )
                }
                else -> Unit
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

@Composable
private fun BottomBarHome(
    tabSelected: MutableState<HomeTabs>,
) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        actions = {
            HomeTabs.entries.forEach { tab ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .imePadding()
                        .pointerInput(Unit) {
                            detectTapGestures { tabSelected.value = tab }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    Icon(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .then(
                                if (tab == tabSelected.value) Modifier.background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    shape = CircleShape
                                )
                                else Modifier
                            )
                            .padding(horizontal = 16.dp)
                            .aspectRatio(1f),
                        painter = painterResource(tab.icon),
                        tint = MaterialTheme.colorScheme.let { if (tab == tabSelected.value) it.primary else it.onSurface },
                        contentDescription = null
                    )

                    Text(
                        text = tab.tittle,
                        fontWeight = if (tab == tabSelected.value) FontWeight.Black else FontWeight.Normal,
                    )
                }
            }
        }
    )
}

@Composable
private fun FABHome(
    tabSelected: HomeTabs,
    onClick: () -> Unit,
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = ImageVector.vectorResource(
                when (tabSelected) {
                    HomeTabs.PRODUCTS -> R.drawable.ic_box_add
                    else -> R.drawable.ic_register_add
                }
            ),
            contentDescription = null
        )
    }
}