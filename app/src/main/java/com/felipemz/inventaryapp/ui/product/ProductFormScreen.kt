package com.felipemz.inventaryapp.ui.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.ui.commons.HorizontalDotDivider
import com.felipemz.inventaryapp.ui.commons.ProductsAddBottomSheet
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnAddProductToPack
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnBack
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnCategoryChanged
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnDescriptionChanged
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnImageChanged
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnNameChanged
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnOpenProduct
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnPackageTypeChanged
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnPriceChanged
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnProductDeleted
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnProductSaved
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnQuantityChanged
import com.felipemz.inventaryapp.ui.product.ProductFormEvent.OnQuantityTypeChanged
import com.felipemz.inventaryapp.ui.product.components.CategoryField
import com.felipemz.inventaryapp.ui.product.components.CommonTitledColumn
import com.felipemz.inventaryapp.ui.product.components.DescriptionField
import com.felipemz.inventaryapp.ui.product.components.EmojiSelectorBottomSheet
import com.felipemz.inventaryapp.ui.product.components.IdProductField
import com.felipemz.inventaryapp.ui.product.components.ImageField
import com.felipemz.inventaryapp.ui.product.components.ImageSelectorBottomSheet
import com.felipemz.inventaryapp.ui.product.components.NameField
import com.felipemz.inventaryapp.ui.product.components.PackageField
import com.felipemz.inventaryapp.ui.product.components.PriceField
import com.felipemz.inventaryapp.ui.product.components.QuantityChangeBottomSheet
import com.felipemz.inventaryapp.ui.product.components.QuantityField
import com.felipemz.inventaryapp.ui.product.components.TopBarProduct

@Composable
internal fun ProductFormScreen(
    state: ProductFormState,
    eventHandler: (ProductFormEvent) -> Unit,
) {

    val scrollState = rememberScrollState()
    var showEmojiPopup by remember { mutableStateOf(false) }
    var showImagePopup by remember { mutableStateOf(false) }
    var showQuantityPopup by remember { mutableStateOf(false) }
    var showProductsPopup by remember { mutableStateOf(false) }

    val moveToFinal = remember {
        suspend { scrollState.animateScrollTo(scrollState.maxValue) }
    }

    when {
        showImagePopup -> ImageSelectorBottomSheet(
            onDismiss = { showImagePopup = false },
            onSelect = {
                eventHandler(OnImageChanged(ProductTypeImage.PhatImage(it)))
                showImagePopup = false
            }
        )
        showEmojiPopup -> EmojiSelectorBottomSheet(
            onDismiss = { showEmojiPopup = false },
            onSelect = {
                eventHandler(OnImageChanged(ProductTypeImage.EmojiImage(it)))
                showEmojiPopup = false
            }
        )
        showQuantityPopup -> QuantityChangeBottomSheet(
            currentQuantity = state.quantity,
            quantityType = state.quantityType ?: QuantityType.UNIT,
            onDismiss = { showQuantityPopup = false },
            onSelect = {
                eventHandler(OnQuantityChanged(it))
                showQuantityPopup = false
            }
        )
        showProductsPopup -> ProductsAddBottomSheet(
            productList = state.productList,
            selected = state.packageType,
            onDismiss = { showProductsPopup = false },
            onSelect = { eventHandler(OnAddProductToPack(it)) }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarProduct(
                onBack = { eventHandler(OnBack) },
                isNewProduct = state.isNewProduct
            )
        },
        bottomBar = {
            BottomBarProductForm(
                enable = state.name.isNotBlank() && state.price > 0 && state.category != null,
                action = { eventHandler(OnProductSaved) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            IdProductField(
                modifier = Modifier.fillMaxWidth(),
                idProduct = state.idProduct,
                isNewProduct = state.isNewProduct
            ) { eventHandler(OnProductDeleted) }

            NameField(
                modifier = Modifier.fillMaxWidth(),
                name = state.name
            ) { eventHandler(OnNameChanged(it)) }

            PriceField(
                modifier = Modifier.fillMaxWidth(),
                value = state.price
            ) { eventHandler(OnPriceChanged(it)) }

            CategoryField(
                modifier = Modifier.fillMaxWidth(),
                category = state.category,
                categories = state.categories
            ) { eventHandler(OnCategoryChanged(it)) }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            ImageField(
                modifier = Modifier.fillMaxWidth(),
                images = state.images,
                imageSelected = state.imageSelected,
                category = state.category
            ) {
                eventHandler(OnImageChanged(it))
                showEmojiPopup = it is ProductTypeImage.EmojiImage
                showImagePopup = it is ProductTypeImage.PhatImage
            }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            DescriptionField(
                modifier = Modifier.fillMaxWidth(),
                description = state.description,
                onOpen = { moveToFinal() }
            ) { eventHandler(OnDescriptionChanged(it)) }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            AdvancedField(
                state = state,
                onPackage = { showProductsPopup = true },
                onQuantity = { showQuantityPopup = true },
                onOpen = { moveToFinal() },
                eventHandler = eventHandler
            )
        }
    }
}

@Composable
private fun AdvancedField(
    state: ProductFormState,
    onQuantity: () -> Unit,
    onPackage: () -> Unit,
    onOpen: suspend () -> Unit,
    eventHandler: (ProductFormEvent) -> Unit
) {
    CommonTitledColumn(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(R.string.copy_advanced),
        isMandatory = null,
        visible = false,
        concealable = true,
        onOpen = { onOpen() }
    ) {

        QuantityField(
            modifier = Modifier.fillMaxWidth(),
            quantityType = state.quantityType,
            isNotPackage = state.packageType.isNull(),
            quantity = state.quantity,
            onAdd = onQuantity,
            onOpen = { onOpen() }
        ) {
            eventHandler(OnQuantityTypeChanged(it))
        }

        PackageField(
            modifier = Modifier.fillMaxWidth(),
            packageType = state.packageType,
            isNotQuantity = state.quantityType.isNull(),
            onAdd = onPackage,
            onOpen = { onOpen() },
            onClick = { eventHandler(OnOpenProduct(it)) },
            onDelete = { eventHandler(ProductFormEvent.OnDeleteProductFromPack(it)) },
        ) {
            eventHandler(OnPackageTypeChanged(it))
        }
    }
}

@Composable
private fun BottomBarProductForm(
    enable: Boolean,
    action: () -> Unit,
) {
    BottomAppBar {
        Button(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            onClick = action,
            enabled = enable
        ) {
            Text(
                text = stringResource(R.string.copy_save),
                fontWeight = FontWeight.Bold
            )
        }
    }
}