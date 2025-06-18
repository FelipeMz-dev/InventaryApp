package com.felipemz.inventaryapp.ui.product_form.field

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.ifFalse
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.orFalse
import com.felipemz.inventaryapp.ui.commons.CommonFormField
import com.felipemz.inventaryapp.ui.commons.CommonTrailingIcon

@Composable
internal fun NameField(
    modifier: Modifier,
    name: String,
    isEnable: Boolean = true,
    onChange: (String) -> Unit,
    onSetEmoji: (String) -> Unit,
) {

    var text by remember(name) { mutableStateOf(name) }

    fun onWriteText(newText: String) {
        val emojis = extractEmojis(newText)
        if (emojis.isNotEmpty()) {
            onSetEmoji(emojis.joinToString(""))
        } else {
            val cleanText = newText.filter {
                it.isLetterOrDigit() || it.isWhitespace()
            }.trimStart()
            text = cleanText
            onChange(cleanText)
        }
    }

    CommonFormField(
        modifier = modifier,
        title = stringResource(id = R.string.copy_name_dots)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            value = text,
            onValueChange = ::onWriteText,
            placeholder = { Text(text = stringResource(R.string.copy_write_here)) },
            trailingIcon = {
                isEnable.ifTrue {
                    CommonTrailingIcon(
                        isTextEmpty = text.isEmpty(),
                        onEdit = onChange
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors().copy(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.outline,
                focusedPlaceholderColor = MaterialTheme.colorScheme.outline
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences
            ),
            enabled = isEnable,
            singleLine = true
        )
    }
}

fun extractEmojis(text: String): List<String> {
    val emojiRegex = Regex(
        "(?:[\uD83C-\uDBFF\uDC00-\uDFFF]+|[\u2600-\u27BF])"
    )
    return emojiRegex.findAll(text)
        .map { it.value }
        .toList()
}