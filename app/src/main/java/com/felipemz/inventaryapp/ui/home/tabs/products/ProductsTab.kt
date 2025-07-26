package com.felipemz.inventaryapp.ui.home.tabs.products

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.core.enums.ProductsOrderBy
import com.felipemz.inventaryapp.domain.model.CategoryModel
import com.felipemz.inventaryapp.ui.LocalProductList
import com.felipemz.inventaryapp.ui.commons.BarcodeScannerDialog
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductEvent
import com.felipemz.inventaryapp.ui.commons.header_product.HeaderProductList
import com.felipemz.inventaryapp.ui.home.HomeEvent
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnCreateProductFromBarcode
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnFocusSearch
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnOpenProduct
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnProductOrderSelected
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnScannerChangeToShow
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnSetCategoryFilterProducts
import com.felipemz.inventaryapp.ui.home.HomeEvent.OnSetNameFilterProducts
import com.felipemz.inventaryapp.ui.home.components.ProductsSortDialog

@Composable
internal fun InventoryTab(
    categories: List<CategoryModel>,
    categorySelected: CategoryModel?,
    isInventory: Boolean,
    isFocusSearch: Boolean,
    isShowScanner: Boolean,
    isShowProductOrderDialog: Boolean,
    productOrderSelected: ProductsOrderBy,
    isProductOrderInverted: Boolean,
    eventHandler: (HomeEvent) -> Unit,
) {

    val products = LocalProductList.current

    if (isShowScanner) {
        BarcodeScannerDialog(
            onDismiss = { eventHandler(OnScannerChangeToShow(false)) },
            onBarcodeScanned = { eventHandler(OnCreateProductFromBarcode(it)) }
        )
    }

    if (isShowProductOrderDialog) {
        ProductsSortDialog(
            productOrderSelected = productOrderSelected,
            isProductOrderInverted = isProductOrderInverted,
            onClose = { order, inverted ->
                eventHandler(OnProductOrderSelected(order, inverted))
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            HeaderProductList(
                modifier = Modifier.fillMaxWidth(),
                isInventory = isInventory,
                isFocusSearch = isFocusSearch,
                categories = categories,
                categorySelected = categorySelected,
            ) { event ->
                when (event) {
                    is HeaderProductEvent.OnFocusSearch -> eventHandler(OnFocusSearch(event.isFocus))
                    is HeaderProductEvent.OnChangeCategory -> eventHandler(OnSetCategoryFilterProducts(event.category))
                    is HeaderProductEvent.OnChangeSearchText -> eventHandler(OnSetNameFilterProducts(event.text))
                }
            }
        }

        if (products.isEmpty()) item {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                text = "No hay resultados",
                textAlign = TextAlign.Center
            )
        } else items(
            items = products,
            key = { it.id }
        ) { item ->
            ProductItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                product = item,
                onClick = { eventHandler(OnOpenProduct(item)) }
            )
        }
    }
}