package com.felipemz.inventaryapp.ui.product_form

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.EMPTY_STRING
import com.felipemz.inventaryapp.core.extensions.hasCameraPermission
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.extensions.isNotNull
import com.felipemz.inventaryapp.core.extensions.isNull
import com.felipemz.inventaryapp.ui.commons.CommonFormField
import com.felipemz.inventaryapp.ui.commons.BarcodeScannerDialog

@Composable
internal fun BarcodeField(
    modifier: Modifier,
    barcode: String?,
    showAlertBarcode: Boolean,
    isEnable: Boolean = true,
    onChange: (String?) -> Unit,
    onOpen: suspend () -> Unit
) {

    val context = LocalContext.current
    var showScanner by remember { mutableStateOf(false) }

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        isGranted.ifTrue { showScanner = true }
    }

    if (showScanner) {
        BarcodeScannerDialog(
            onBarcodeScanned = { code ->
                onChange(code)
                showScanner = false
            },
            onDismiss = { showScanner = false }
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
                onClick = {
                    if (context.hasCameraPermission()) showScanner = true
                    else requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_scanner),
                    contentDescription = "Escanear código de barras"
                )
            }
        }
    }
}

