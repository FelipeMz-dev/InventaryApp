package com.felipemz.inventaryapp.ui.product_form.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R

@Composable
internal fun DescriptionField(
    modifier: Modifier,
    description: String,
    onOpen: suspend () -> Unit,
    onChange: (String) -> Unit
) {

    var text by remember(description) { mutableStateOf(description) }

    CommonTitledColumn(
        modifier = modifier,
        title = stringResource(R.string.copy_description_dots),
        concealable = true,
        visible = false,
        isMandatory = false,
        onOpen = onOpen
    ) {
        OutlinedTextField(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            value = text,
            onValueChange = {
                val newText = it.trimStart()
                text = newText
                onChange(newText)
            },
            placeholder = { Text(text = stringResource(R.string.copy_write_here)) },
            trailingIcon = {
                CommonTrailingIcon(
                    isTextEmpty = text.isEmpty(),
                    onEdit = onChange
                )
            },
            colors = OutlinedTextFieldDefaults.colors().copy(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
                focusedPlaceholderColor = MaterialTheme.colorScheme.outline
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Sentences
            ),
            maxLines = 6,
            minLines = 4,
        )
    }
}