package com.felipemz.inventaryapp.ui.product_form

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
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.domain.model.ProductTypeImage
import com.felipemz.inventaryapp.ui.commons.CommonFormField
import com.felipemz.inventaryapp.ui.commons.HorizontalDotDivider
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnBack
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnBarcodeChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnCategoryChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnCostChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnDeleteCategory
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnDescriptionChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnImageChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnInsertOrUpdateCategory
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnNameChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnOpenProduct
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnPackageAction
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnPriceChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnProductSaved
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnQuantityChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnQuantityTypeChanged
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnSetCategoryFilter
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnSetNameFilter
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnSortCategories
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnTogglePackage
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.OnTryDeleteProduct
import com.felipemz.inventaryapp.ui.product_form.components.EmojiSelectorBottomSheet
import com.felipemz.inventaryapp.ui.product_form.components.ImageSelectorBottomSheet
import com.felipemz.inventaryapp.ui.product_form.components.TopBarProduct
import com.felipemz.inventaryapp.ui.product_form.components.alert_dialog.AlertDialogProductForm
import com.felipemz.inventaryapp.ui.product_form.field.CategoryField
import com.felipemz.inventaryapp.ui.product_form.field.CostField
import com.felipemz.inventaryapp.ui.product_form.field.DescriptionField
import com.felipemz.inventaryapp.ui.product_form.field.IdProductField
import com.felipemz.inventaryapp.ui.product_form.field.ImageField
import com.felipemz.inventaryapp.ui.product_form.field.NameField
import com.felipemz.inventaryapp.ui.product_form.field.PackageField
import com.felipemz.inventaryapp.ui.product_form.field.PriceField
import com.felipemz.inventaryapp.ui.product_form.field.QuantityField

@Composable
internal fun ProductFormScreen(
    state: ProductFormState,
    eventHandler: (ProductFormEvent) -> Unit,
) {

    val scrollState = rememberScrollState()
    val showImageField = remember { mutableStateOf(false) }
    var showEmojiPopup by remember { mutableStateOf(false) }
    var showImagePopup by remember { mutableStateOf(false) }

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
    }

    AlertDialogProductForm(
        alertDialog = state.alertDialog,
        eventHandler = eventHandler
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarProduct(
                onBack = { eventHandler(OnBack) },
                isNewProduct = state.editProduct.isNull()
            )
        },
        bottomBar = {
            BottomBarProductForm(
                enable = state.enableToSave,
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
                idProduct = state.editProduct?.id,
                canDelete = state.editProduct.isNotNull()
                        && state.categoryIdToChange.isNull()
                        && state.packageIdToChange.isNull(),
            ) { eventHandler(OnTryDeleteProduct) }

            NameField(
                modifier = Modifier.fillMaxWidth(),
                name = state.name,
                isEnable = state.categoryIdToChange.isNull()
                        && state.packageIdToChange.isNull(),
                onChange = { eventHandler(OnNameChanged(it)) },
            ) {
                showImageField.value = true
                eventHandler(OnImageChanged(ProductTypeImage.EmojiImage(it)))
            }

            PriceField(
                modifier = Modifier.fillMaxWidth(),
                value = state.price,
                isEnable = state.categoryIdToChange.isNull()
                        && state.packageIdToChange.isNull(),
            ) { eventHandler(OnPriceChanged(it)) }

            CategoryField(
                modifier = Modifier.fillMaxWidth(),
                category = state.category,
                categories = state.categories,
                categoryIdToChange = state.categoryIdToChange,
                isEnable = state.packageIdToChange.isNull(),
                onInsertOrUpdate = { eventHandler(OnInsertOrUpdateCategory(it)) },
                onDelete = { eventHandler(OnDeleteCategory(it.id)) },
                onSort = { from, to -> eventHandler(OnSortCategories(from, to)) },
            ) { eventHandler(OnCategoryChanged(it)) }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            ImageField(
                modifier = Modifier.fillMaxWidth(),
                images = state.images,
                imageSelected = state.imageSelected,
                category = state.category,
                isEnable = state.categoryIdToChange.isNull()
                        && state.packageIdToChange.isNull(),
                isVisible = showImageField,
                onOpen = { moveToFinal() }
            ) {
                eventHandler(OnImageChanged(it))
                showEmojiPopup = it is ProductTypeImage.EmojiImage
                showImagePopup = it is ProductTypeImage.PhatImage
            }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            DescriptionField(
                modifier = Modifier.fillMaxWidth(),
                description = state.description,
                isEnable = state.categoryIdToChange.isNull()
                        && state.packageIdToChange.isNull(),
                onOpen = { moveToFinal() }
            ) { eventHandler(OnDescriptionChanged(it)) }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            AdvancedField(
                state = state,
                onOpen = { moveToFinal() },
                eventHandler = eventHandler
            )
        }
    }
}

@Composable
private fun AdvancedField(
    state: ProductFormState,
    onOpen: suspend () -> Unit,
    eventHandler: (ProductFormEvent) -> Unit
) {
    CommonFormField(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(R.string.copy_advanced),
        isMandatory = null,
        visible = state.barcodeCreation
                || state.packageIdToChange.isNotNull(),
        concealable = true,
        onOpen = { onOpen() }
    ) {
        BarcodeField(
            modifier = Modifier.fillMaxWidth(),
            barcode = state.barcode,
            showAlertBarcode = state.alertBarcode,
            isEnable = !state.barcodeCreation && state.categoryIdToChange.isNull()
                    && state.packageIdToChange.isNull(),
            onChange = { eventHandler(OnBarcodeChanged(it)) },
            onOpen = { onOpen() }
        )

        CostField(
            modifier = Modifier.fillMaxWidth(),
            value = state.cost,
            isEnable = state.categoryIdToChange.isNull()
                    && state.packageIdToChange.isNull(),
            onChange = { eventHandler(OnCostChanged(it)) },
            onOpen = { onOpen() }
        )

        QuantityField(
            modifier = Modifier.fillMaxWidth(),
            quantityType = state.quantityType,
            isEnabled = state.packageList.isNull()
                    && state.categoryIdToChange.isNull()
                    && state.packageIdToChange.isNull(),
            quantity = state.quantity,
            onOpen = { onOpen() },
            onChange = { eventHandler(OnQuantityChanged(it)) }
        ) { eventHandler(OnQuantityTypeChanged(it)) }

        PackageField(
            modifier = Modifier.fillMaxWidth(),
            selectedProducts = state.packageList,
            categories = state.categories,
            isEnabled = state.categoryIdToChange.isNull()
                    && state.quantityType.isNull(),
            onOpen = onOpen,
            onSetNameFilter = { eventHandler(OnSetNameFilter(it)) },
            onSetCategoryFilter = { eventHandler(OnSetCategoryFilter(it)) },
            onClick = { eventHandler(OnOpenProduct(it)) },
            onSelect = { eventHandler(OnPackageAction(it)) },
            toggle = { eventHandler(OnTogglePackage(it)) }
        )
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

