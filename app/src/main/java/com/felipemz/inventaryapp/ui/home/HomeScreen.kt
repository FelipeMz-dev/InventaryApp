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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.felipemz.inventaryapp.core.charts.RangeDateChart
import com.felipemz.inventaryapp.core.enums.MovementsFilterChip
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.core.extensions.ifFalse
import com.felipemz.inventaryapp.ui.commons.BarcodeScannerDialog
import com.felipemz.inventaryapp.ui.home.HomeEvent.Init
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnCreateProductFromBarcode
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnFAB
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnLabelSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnMovementFilterSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnMovementsInverted
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnProductOrderSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnReportsCustomFilterSelected
import com.felipemz.inventaryapp.ui.home.components.BottomBarHome
import com.felipemz.inventaryapp.ui.home.components.FABHome
import com.felipemz.inventaryapp.ui.home.components.MovementLabelDialog
import com.felipemz.inventaryapp.ui.home.components.ProductsSortDialog
import com.felipemz.inventaryapp.ui.home.components.ReportsCalendarDialog
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

    var isShowLabelPopup by remember { mutableStateOf(false) }
    var isProductOrderPopup by remember { mutableStateOf(false) }
    var isReportsCalendarPopup by remember { mutableStateOf(false) }
    var showScanner by remember { mutableStateOf(false) }


    val tabSelected = remember { mutableStateOf(HomeTabs.MOVEMENTS) }
    val isReports = remember(tabSelected.value) {
        tabSelected.value == HomeTabs.REPORTS
    }

    LaunchedEffect(Unit) { eventHandler(Init) }

    HomeDialogs(
        isShowLabelPopup = isShowLabelPopup,
        isReportsCalendarPopup = isReportsCalendarPopup,
        isProductOrderPopup = isProductOrderPopup,
        isShowScanner = showScanner,
        state = state,
        onDismissLabel = { isShowLabelPopup = false },
        onLabelSelected = { eventHandler(OnLabelSelected(it)) },
        onDismissReports = { isReportsCalendarPopup = false },
        onReportsSelected = { eventHandler(OnReportsCustomFilterSelected(it)) },
        onOrderSelected = { order, inverted ->
            eventHandler(OnProductOrderSelected(order, inverted))
            isProductOrderPopup = false
        },
        onDismissScanner = { showScanner = false },
        onBarcodeScanned = { eventHandler(OnCreateProductFromBarcode(it)) }
    )

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
                        HomeTabs.PRODUCTS -> isProductOrderPopup = true
                        HomeTabs.MOVEMENTS -> eventHandler(OnMovementsInverted(!state.isMovementsInverted))
                        HomeTabs.REPORTS -> isReportsCalendarPopup = true
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
                            showScanner = true
                        }
                    }
                )
            }
        },
    ) {
        TabsContentBody(
            modifier = Modifier.padding(it),
            tabSelected = tabSelected,
            state = state,
            eventHandler = { event ->
                if (event is OnMovementFilterSelected) {
                    isShowLabelPopup = event.filter == MovementsFilterChip.LABEL
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

    LaunchedEffect(tabSelected.value, pagerState.currentPage, pagerState.isScrollInProgress) {
        if (pagerState.isScrollInProgress) {
            tabSelected.value = HomeTabs.entries[pagerState.currentPage]
        } else {
            val tab = tabSelected.value.ordinal
            if (pagerState.currentPage != tab) {
                pagerState.animateScrollToPage(tab)
            } else {
                focusManager.clearFocus()
            }
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
    ) { page ->
        when (page) {
            HomeTabs.PRODUCTS.ordinal -> InventoryTab(
                categories = state.categories,
                categorySelected = state.categorySelected,
                isInventory = pagerState.settledPage == HomeTabs.PRODUCTS.ordinal,
                isFocusSearch = state.isSearchFocused,
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

@Composable
private fun HomeDialogs(
    isShowLabelPopup: Boolean,
    isReportsCalendarPopup: Boolean,
    isProductOrderPopup: Boolean,
    isShowScanner: Boolean,
    state: HomeState,
    onDismissLabel: () -> Unit,
    onLabelSelected: (String) -> Unit,
    onDismissReports: () -> Unit,
    onReportsSelected: (RangeDateChart) -> Unit,
    onOrderSelected: (ProductsOrderBy, Boolean) -> Unit,
    onDismissScanner: () -> Unit,
    onBarcodeScanned: (String) -> Unit,
) {
    if (isShowLabelPopup) {
        MovementLabelDialog(
            labelList = state.movementLabelList,
            onDismiss = onDismissLabel,
            onSelect = onLabelSelected
        )
    }
    if (isReportsCalendarPopup) {
        ReportsCalendarDialog(
            onDismiss = onDismissReports,
            onAccept = onReportsSelected
        )
    }
    if (isProductOrderPopup) {
        ProductsSortDialog(
            productOrderSelected = state.productOrderSelected,
            isProductOrderInverted = state.isProductOrderInverted,
            onClose = onOrderSelected
        )
    }
    if (isShowScanner) {
        BarcodeScannerDialog(
            onDismiss = onDismissScanner,
            onBarcodeScanned = onBarcodeScanned
        )
    }
}