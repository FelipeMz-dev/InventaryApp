package com.felipemz.inventaryapp.ui.product_form

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.coerceIn
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.enums.QuantityType
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import com.felipemz.inventaryapp.core.utils.PriceUtil
import com.felipemz.inventaryapp.ui.commons.HorizontalDotDivider
import com.felipemz.inventaryapp.ui.commons.ProductsAddBottomSheet
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent.*
import com.felipemz.inventaryapp.ui.product_form.components.CategoryField
import com.felipemz.inventaryapp.ui.product_form.components.CommonFormField
import com.felipemz.inventaryapp.ui.product_form.components.CommonTrailingIcon
import com.felipemz.inventaryapp.ui.product_form.components.CompositionField
import com.felipemz.inventaryapp.ui.product_form.components.CostField
import com.felipemz.inventaryapp.ui.product_form.components.DescriptionField
import com.felipemz.inventaryapp.ui.product_form.components.EmojiSelectorBottomSheet
import com.felipemz.inventaryapp.ui.product_form.components.IdProductField
import com.felipemz.inventaryapp.ui.product_form.components.ImageField
import com.felipemz.inventaryapp.ui.product_form.components.ImageSelectorBottomSheet
import com.felipemz.inventaryapp.ui.product_form.components.NameField
import com.felipemz.inventaryapp.ui.product_form.components.PackageField
import com.felipemz.inventaryapp.ui.product_form.components.PriceField
import com.felipemz.inventaryapp.ui.product_form.components.QuantityChangeBottomSheet
import com.felipemz.inventaryapp.ui.product_form.components.QuantityField
import com.felipemz.inventaryapp.ui.product_form.components.TopBarProduct

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
            selected = state.packageProduct?.let {
                listOf(it)
            } ?: state.compositionProducts ?: emptyList(),
            onDismiss = { showProductsPopup = false },
            onSelect = {eventHandler(OnSubProductSelect(it)) }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarProduct(
                onBack = { eventHandler(OnBack) },
                isNewProduct = state.originalProduct.isNull()
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
                idProduct = state.originalProduct?.id,
                isNewProduct = state.originalProduct.isNull()
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
                category = state.category,
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
                onOpen = { moveToFinal() }
            ) { eventHandler(OnDescriptionChanged(it)) }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            CostField(
                modifier = Modifier.fillMaxWidth(),
                value = state.cost,
                onChange = { eventHandler(OnCostChanged(it)) },
                onOpen = { moveToFinal() }
            )

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            AdvancedField(
                state = state,
                onAddSubProduct = { showProductsPopup = true },
                onQuantity = { showQuantityPopup = true },
                onOpen = { moveToFinal() },
                eventHandler = eventHandler
            )
        }
    }
}

@Composable
private fun MessengerToast(message: String?){

    val context = LocalContext.current

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun AdvancedField(
    state: ProductFormState,
    onQuantity: () -> Unit,
    onAddSubProduct: () -> Unit,
    onOpen: suspend () -> Unit,
    eventHandler: (ProductFormEvent) -> Unit
) {
    CommonFormField(
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
            enabled = state.packageProduct.isNull() && state.compositionProducts.isNull(),
            quantity = state.quantity,
            onAdd = onQuantity,
            onOpen = { onOpen() }
        ) { eventHandler(OnQuantityTypeChanged(it)) }

        PackageField(
            modifier = Modifier.fillMaxWidth(),
            packageProduct = state.packageProduct,
            isNotQuantity = state.quantityType.isNull(),
            isNotComposition = state.compositionProducts.isNull(),
            onAdd = onAddSubProduct,
            onOpen = onOpen,
            onClick = { eventHandler(OnOpenProduct(it)) },
            onSelect = { eventHandler(OnPackageProductSelect(it)) }
        )

        CompositionField(
            modifier = Modifier.fillMaxWidth(),
            compositionProducts = state.compositionProducts,
            isNotQuantity = state.quantityType.isNull(),
            isNotPackage = state.packageProduct.isNull(),
            onAdd = onAddSubProduct,
            onOpen = onOpen,
            onClick = { eventHandler(OnOpenProduct(it)) },
            onSelect = { eventHandler(OnCompositionProductSelect(it)) }
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