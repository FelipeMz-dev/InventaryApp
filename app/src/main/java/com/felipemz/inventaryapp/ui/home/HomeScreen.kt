package com.felipemz.inventaryapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.extensions.ifFalse
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnFAB
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnLabelChangeToShow
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnMovementFilterSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnMovementsInverted
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnProductsOrderChangeToShow
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnReportsCalendarChangeToShow
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnScannerChangeToShow
import com.felipemz.inventaryapp.ui.home.components.BottomBarHome
import com.felipemz.inventaryapp.ui.home.components.FABHome
import com.felipemz.inventaryapp.ui.home.components.TopBarHome
import com.felipemz.inventaryapp.ui.home.tabs.HomeTabs
import com.felipemz.inventaryapp.ui.home.tabs.movements.MovementsTab
import com.felipemz.inventaryapp.ui.home.tabs.products.InventoryTab
import com.felipemz.inventaryapp.ui.home.tabs.reports.ReportsTab

@Composable
internal fun HomeScreen(
    state: HomeState,
    eventHandler: (HomeEvent) -> Unit,
) {

    val tabSelected = remember { mutableStateOf(HomeTabs.MOVEMENTS) }
    val isReports = tabSelected.value == HomeTabs.REPORTS

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            CustomVerticalAnimatedVisibility(!state.isSearchFocused) {
                TopBarHome(
                    tabSelected = tabSelected.value,
                    isMovementsInverted = state.isMovementsInverted,
                    isProductOrderInverted = state.isProductOrderInverted,
                ) {
                    when (it) {
                        HomeTabs.PRODUCTS -> eventHandler(OnProductsOrderChangeToShow(true))
                        HomeTabs.MOVEMENTS -> eventHandler(OnMovementsInverted(!state.isMovementsInverted))
                        HomeTabs.REPORTS -> eventHandler(OnReportsCalendarChangeToShow(true))
                    }
                }
            }
        },
        bottomBar = {
            CustomVerticalAnimatedVisibility(!state.isSearchFocused) {
                BottomBarHome(tabSelected)
            }
        },
        floatingActionButton = {
            isReports.ifFalse {
                FABHome(
                    tabSelected = tabSelected.value,
                    onClick = {
                        eventHandler(OnFAB(tabSelected.value))
                    },
                    onClickSmall = {
                        if (tabSelected.value == HomeTabs.PRODUCTS) {
                            eventHandler(OnScannerChangeToShow(true))
                        }
                    }
                )
            }
        }
    ) {
        TabsContentBody(
            modifier = Modifier.padding(it),
            tabSelected = tabSelected,
            state = state,
            eventHandler = { event ->
                if (event is OnMovementFilterSelected) {
                    eventHandler(OnLabelChangeToShow(event.filter == MovementsFilterChip.LABEL))
                }
                eventHandler(event)
            }
        )
    }
}

@Composable
fun CustomVerticalAnimatedVisibility(
    visible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        content()
    }
}

@Composable
private fun TabsContentBody(
    modifier: Modifier,
    tabSelected: MutableState<HomeTabs>,
    state: HomeState,
    eventHandler: (HomeEvent) -> Unit,
) {
    val pagerState = rememberPagerState(tabSelected.value.ordinal) { HomeTabs.entries.size }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(tabSelected.value) {
        if (pagerState.currentPage != tabSelected.value.ordinal) {
            pagerState.animateScrollToPage(tabSelected.value.ordinal)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (tabSelected.value.ordinal != pagerState.currentPage) {
            tabSelected.value = HomeTabs.entries[pagerState.currentPage]
            focusManager.clearFocus()
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = !state.isSearchFocused
    ) { page ->
        when (page) {
            HomeTabs.PRODUCTS.ordinal -> InventoryTab(
                categories = state.categories,
                categorySelected = state.categorySelected,
                isInventory = tabSelected.value == HomeTabs.PRODUCTS,
                isFocusSearch = state.isSearchFocused,
                isShowScanner = state.showScanner,
                isShowProductOrderDialog = state.showProductOrderDialog,
                productOrderSelected = state.productOrderSelected,
                isProductOrderInverted = state.isProductOrderInverted,
                eventHandler = eventHandler,
            )
            HomeTabs.MOVEMENTS.ordinal -> MovementsTab(
                movements = state.movements,
                chipSelected = state.movementFilterSelected,
                labelSelected = state.movementLabelSelected,
                movementLabelList = state.movementLabelList,
                isShowLabelPopup = state.isShowLabelPopup,
                eventHandler = eventHandler
            )
            HomeTabs.REPORTS.ordinal -> ReportsTab(
                chipSelected = state.reportsFilterChipSelected,
                customDateSelected = state.reportsCustomFilterSelected,
                isShowReportsCalendarDialog = state.showReportsCalendarDialog,
                eventHandler = eventHandler
            )
        }
    }
}