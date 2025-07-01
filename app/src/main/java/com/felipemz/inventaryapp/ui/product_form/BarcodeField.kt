package com.felipemz.inventaryapp.ui.product_form

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.EMPTY_STRING
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.ui.commons.BarcodeScannerDialog
import com.felipemz.inventaryapp.ui.commons.CommonFormField

@Composable
internal fun BarcodeField(
    modifier: Modifier,
    barcode: String?,
    showAlertBarcode: Boolean,
    isEnable: Boolean = true,
    onChange: (String?) -> Unit,
    onOpen: suspend () -> Unit
) {

    var showScanner by remember { mutableStateOf(false) }

    if (showScanner) {
        BarcodeScannerDialog(
            onDismiss = { showScanner = false },
            onBarcodeScanned = { code ->
                onChange(code)
                showScanner = false
            }
        )
    }

    CommonFormField(
        modifier = modifier,
        title = "Codigo de barras:",
        isMandatory = false,
        visible = barcode.isNotNull(),
        concealable = true,
        onOpen = onOpen,
        thumbContent = {
            Switch(
                enabled = isEnable,
                checked = barcode.isNotNull(),
                onCheckedChange = { state ->
                    onChange(if (barcode.isNull()) EMPTY_STRING else null)
                }
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(OutlinedTextFieldDefaults.shape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = OutlinedTextFieldDefaults.shape
                )
                .padding(
                    vertical = 4.dp,
                    horizontal = 8.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_barcode),
                contentDescription = "Codigo de barras",
                tint = MaterialTheme.colorScheme.outline
            )

            Text(
                modifier = Modifier.weight(1f),
                text = barcode?.takeIf { it.isNotEmpty() } ?: "Codigo de barras",
                color = if (barcode.isNullOrEmpty()) MaterialTheme.colorScheme.outline
                else MaterialTheme.colorScheme.onSurface,
            )

            showAlertBarcode.ifTrue {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_warning),
                    contentDescription = "Alerta de codigo de barras",
                    tint = Color.Unspecified
                )
            }

            FilledIconButton(
                shape = RoundedCornerShape(12.dp),
                enabled = isEnable,
                onClick = { showScanner = true }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_scanner),
                    contentDescription = "Escanear código de barras"
                )
            }
        }
    }
}

