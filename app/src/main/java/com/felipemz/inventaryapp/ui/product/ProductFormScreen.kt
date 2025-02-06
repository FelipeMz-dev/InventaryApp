package com.felipemz.inventaryapp.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.coerceIn
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.ifNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.core.extensions.tryOrDefault
import com.felipemz.inventaryapp.core.utils.PriceUtil
import com.felipemz.inventaryapp.ui.commons.HorizontalDotDivider
import com.felipemz.inventaryapp.ui.home.tabs.products.ProductTypeImage
import com.felipemz.inventaryapp.ui.product.components.CategoryField
import com.felipemz.inventaryapp.ui.product.components.CommonTitledColumn
import com.felipemz.inventaryapp.ui.product.components.CommonTrailingIcon
import com.felipemz.inventaryapp.ui.product.components.DescriptionField
import com.felipemz.inventaryapp.ui.product.components.EmojiSelectorBottomSheet
import com.felipemz.inventaryapp.ui.product.components.ImageField
import com.felipemz.inventaryapp.ui.product.components.ImageSelectorBottomSheet
import com.felipemz.inventaryapp.ui.product.components.NameField
import com.felipemz.inventaryapp.ui.product.components.PriceField
import com.felipemz.inventaryapp.ui.product.components.QuantityChangeBottomSheet
import com.felipemz.inventaryapp.ui.product.components.TopBarProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductFormScreen(
    state: ProductFormState,
    eventHandler: (ProductFormEvent) -> Unit,
) {

    var showEmojiPopup by remember { mutableStateOf(false) }
    var showImagePopup by remember { mutableStateOf(false) }
    var shoQuantityPopup by remember { mutableStateOf(false) }

    when {
        showImagePopup -> ImageSelectorBottomSheet(
            onDismiss = { showImagePopup = false },
            onSelect = {
                eventHandler(ProductFormEvent.OnImageChanged(ProductTypeImage.PhatImage(it)))
                showImagePopup = false
            }
        )
        showEmojiPopup -> EmojiSelectorBottomSheet(
            onDismiss = { showEmojiPopup = false },
            onSelect = {
                eventHandler(ProductFormEvent.OnImageChanged(ProductTypeImage.EmojiImage(it)))
                showEmojiPopup = false
            }
        )
        shoQuantityPopup -> QuantityChangeBottomSheet(
            currentQuantity = state.quantity,
            quantityType = state.quantityType ?: QuantityType.UNIT,
            onDismiss = { shoQuantityPopup = false },
            onSelect = {
                eventHandler(ProductFormEvent.OnQuantityChanged(it))
                shoQuantityPopup = false
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarProduct(
                onBack = { eventHandler(ProductFormEvent.OnBack) },
                onDelete = { eventHandler(ProductFormEvent.OnProductDeleted) },
                isNewProduct = state.isNewProduct
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onClick = { eventHandler(ProductFormEvent.OnProductSaved) },
                enabled = state.name.isNotBlank() && state.price > 0 && state.category != null
            ) { Text(text = stringResource(R.string.copy_save)) }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = getIdString(state.idProduct),
                fontWeight = FontWeight.Bold,
            )

            NameField(
                modifier = Modifier.fillMaxWidth(),
                name = state.name
            ) { eventHandler(ProductFormEvent.OnNameChanged(it)) }

            PriceField(
                modifier = Modifier.fillMaxWidth(),
                value = state.price
            ) { eventHandler(ProductFormEvent.OnPriceChanged(it)) }

            CategoryField(
                modifier = Modifier.fillMaxWidth(),
                category = state.category,
                categories = state.categories
            ) { eventHandler(ProductFormEvent.OnCategoryChanged(it)) }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            ImageField(
                modifier = Modifier.fillMaxWidth(),
                images = state.images,
                imageSelected = state.imageSelected,
                category = state.category
            ) {
                eventHandler(ProductFormEvent.OnImageChanged(it))
                showEmojiPopup = it is ProductTypeImage.EmojiImage
                showImagePopup = it is ProductTypeImage.PhatImage
            }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            DescriptionField(
                modifier = Modifier.fillMaxWidth(),
                description = state.description
            ) { eventHandler(ProductFormEvent.OnDescriptionChanged(it)) }

            HorizontalDotDivider(modifier = Modifier.fillMaxWidth())

            QuantityField(
                modifier = Modifier.fillMaxWidth(),
                quantityType = state.quantityType,
                quantity = state.quantity,
                onAdd = { shoQuantityPopup = true }
            ) { eventHandler(ProductFormEvent.OnQuantityTypeChanged(it)) }
        }
    }
}

@Composable
private fun QuantityField(
    modifier: Modifier,
    quantityType: QuantityType?,
    quantity: Int = 0,
    onAdd: () -> Unit,
    onSelect: (QuantityType?) -> Unit = {},
) {

    val toggle by remember(quantityType) { derivedStateOf { quantityType.isNull().not() } }
    var showListPopup by remember { mutableStateOf(false) }

    CommonTitledColumn(
        modifier = modifier,
        title = "Cantidades:",
        concealable = true,
        isMandatory = false,
        visible = toggle,
        thumbContent = {
            Switch(
                checked = toggle,
                onCheckedChange = {
                    if (it) onSelect(QuantityType.UNIT) else onSelect(null)
                }
            )
        }
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Tipo de cantidad:",
                color = MaterialTheme.colorScheme.outline
            )

            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { showListPopup = true }
                    .padding(horizontal = 8.dp),
                text = quantityType?.text.orEmpty(),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }

        Row {

            Text(
                text = "Cantidad:",
                color = MaterialTheme.colorScheme.outline
            )

            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onAdd() }
                    .padding(start = 8.dp),
                text = "$quantity/${quantityType?.initial.orEmpty()}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }

        DropdownMenu(
            modifier = Modifier.padding(8.dp),
            expanded = showListPopup,
            onDismissRequest = { showListPopup = false },
            shape = RoundedCornerShape(8.dp),
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
        ) {
            QuantityType.entries.forEach { type ->
                Text(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable {
                            showListPopup = false
                            onSelect(type)
                        }
                        .padding(horizontal = 8.dp),
                    text = type.text,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

enum class QuantityType(
    val text: String,
    val initial: String
) {
    UNIT("Unidad", "u"),
    OUNCE("Onza", "Oz"),
    GRAM("Gramo", "g"),
    KILOGRAM("Kilogramo", "Kg"),
    MILLIGRAM("Miligramo", "mg"),
    LIBRA("Libra", "Lb"),
    LITER("Litro", "L"),
    MILLILITER("Mililitro", "mL"),
    METER("Metro", "m"),
    CENTIMETER("Centímetro", "cm"),
    MILLIMETER("Milímetro", "mm"), ;
}

@Composable
private fun getIdString(id: Int?) = buildAnnotatedString {
    append(stringResource(R.string.copy_id_dots))
    withStyle(
        style = SpanStyle(
            fontWeight = FontWeight.Light,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.outline
        ),
    ) {
        append(
            id?.toString()?.padStart(
                length = 6,
                padChar = '0'
            ) ?: stringResource(R.string.copy_autogenerated)
        )
    }
}

