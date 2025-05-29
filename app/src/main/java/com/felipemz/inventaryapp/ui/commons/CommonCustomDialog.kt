package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CommonCustomDialog(
    title: String,
    onDismiss: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Header(
                    modifier = Modifier.fillMaxWidth(),
                    title = title,
                    icon = icon,
                    onClose = onDismiss
                )

                content()
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier,
    title: String,
    icon: (@Composable () -> Unit)?,
    onClose: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        icon?.invoke()

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClose) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null
            )
        }
    }
}