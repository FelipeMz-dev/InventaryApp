package com.felipemz.inventaryapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.HomeTabs
import com.felipemz.inventaryapp.core.extensions.ifFalse
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.ui.home.components.BottomBarHome
import com.felipemz.inventaryapp.ui.home.components.MovementLabelPopup
import com.felipemz.inventaryapp.ui.home.components.ProductsOrderPopup
import com.felipemz.inventaryapp.ui.home.components.ReportsCalendarPopup
import com.felipemz.inventaryapp.ui.home.components.TopBarHome
import com.felipemz.inventaryapp.ui.home.tabs.movements.MovementsTab
import com.felipemz.inventaryapp.ui.home.tabs.products.InventoryTab
import com.felipemz.inventaryapp.ui.home.tabs.reports.ReportsTab

@Composable
internal fun HomeScreen(
    state: HomeState,
    eventHandler: (HomeEvent) -> Unit,
) {

    LaunchedEffect(Unit) { eventHandler(HomeEvent.Init) }

    val tabSelected = remember { mutableStateOf(HomeTabs.MOVEMENTS) }
    val pagerState = rememberPagerState(1) { HomeTabs.entries.size }
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
                AnimatedVisibility(!state.isSearchFocused) {
                    TopBarHome(
                        tabSelected = tabSelected.value,
                        isMovementsInverted = state.isMovementsInverted,
                        isProductOrderInverted = state.isProductOrderInverted,
                        eventHandler = eventHandler
                    )
                }
            },
            bottomBar = {
                AnimatedVisibility(!state.isSearchFocused) {
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
                        categories = state.categories,
                        categorySelected = state.categorySelected,
                        isInventory = pagerState.settledPage == HomeTabs.PRODUCTS.ordinal,
                        isFocusSearch = state.isSearchFocused,
                        products = state.products,
                        eventHandler = eventHandler,
                    )
                    HomeTabs.MOVEMENTS.ordinal -> MovementsTab(
                        movements = state.movements,
                        date = state.currentDate,
                        total = state.totalAmount,
                        chipSelected = state.movementFilterSelected,
                        labelSelected = state.movementLabelSelected,
                        eventHandler = eventHandler
                    )
                    HomeTabs.REPORTS.ordinal -> ReportsTab(
                        chipSelected = state.reportsFilterChipSelected,
                        customDateSelected = state.reportsCustomFilterSelected,
                        eventHandler = eventHandler
                    )
                }
            }
        }

        state.isShowLabelPopup.ifTrue {
            MovementLabelPopup(
                labelList = state.movementLabelList,
                onSelect = { eventHandler(HomeEvent.OnLabelSelected(it)) }
            ) { eventHandler(HomeEvent.OnHideLabelPopup) }
        }

        state.isProductOrderPopup.ifTrue {
            ProductsOrderPopup(
                productOrderSelected = state.productOrderSelected,
                isProductOrderInverted = state.isProductOrderInverted
            ) { orderBy, isOrderInverted ->
                eventHandler(HomeEvent.OnProductOrderSelected(orderBy, isOrderInverted))
            }
        }

        state.isReportsCalendarPopup.ifTrue {
            ReportsCalendarPopup(
                onAccept = { eventHandler(HomeEvent.OnReportsCustomFilterSelected(it)) },
            ) { eventHandler(HomeEvent.OnCloseReportsCalendarPopup) }
        }
    }
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
                    HomeTabs.PRODUCTS -> R.drawable.ic_product_add
                    else -> R.drawable.ic_register_add
                }
            ),
            contentDescription = null
        )
    }
}