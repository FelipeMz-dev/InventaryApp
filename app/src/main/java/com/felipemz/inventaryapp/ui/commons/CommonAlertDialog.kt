package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommonAlertDialog(
    message: String,
    textAction: String,
    body: @Composable () -> Unit = {},
    canAction: Boolean = true,
    onDismiss: () -> Unit,
    onAction: () -> Unit = onDismiss
) {
    AlertDialog(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(4.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_warning),
                    tint = Color.Unspecified,
                    contentDescription = "Warning"
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = "Advertencia",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null
                    )
                }
            }
        },
        text = {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                body()
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                modifier = Modifier.Companion.fillMaxWidth(),
                onClick = onAction,
                enabled = canAction,
            ) {
                Text(text = textAction)
            }
        }
    )
}