package com.felipemz.inventaryapp.ui.commons

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.core.utils.BarcodeCameraHandler
import com.felipemz.inventaryapp.ui.animation.scanAnimation
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun BarcodeScannerDialog(
    onBarcodeScanned: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val handler = remember {
        BarcodeCameraHandler(
            ctx = context,
            lifecycleOwner = lifecycleOwner,
            onBarcodeScanned = onBarcodeScanned,
            onDismiss = onDismiss
        )
    }
    val previewView = remember { PreviewView(context) }

    val close = {
        handler.terminate()
        onDismiss()
    }

    Dialog(close) {
        Column(
            Modifier.Companion
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .widthIn(max = 320.dp)
        ) {

            TopBarScanner(close)

            Box(
                modifier = Modifier.Companion
                    .size(300.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(Color.Companion.Black)
                    .scanAnimation()
            ) {
                AndroidView(
                    modifier = Modifier.Companion
                        .matchParentSize()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                val factory = previewView.meteringPointFactory
                                val point = factory.createPoint(offset.x, offset.y)
                                handler.setFocus(point)
                            }
                        },
                    factory = { ctx ->
                        previewView.also { handler.setupCamera(it, cameraProviderFuture) }
                    }
                )
            }

            BottomActionsScanner(
                isTorchEnabled = handler.isTorchEnabled.value,
                onTorch = { handler.toggleTorch() },
                onFlip = {
                    handler.isTorchEnabled.value.ifTrue { handler.toggleTorch() }
                    handler.switchCamera(previewView, cameraProviderFuture)
                }
            )
        }
    }
}

@Composable
private fun TopBarScanner(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier.Companion.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Escanea un código de barras",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Companion.Bold,
            modifier = Modifier.Companion.padding(8.dp)
        )

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun BottomActionsScanner(
    isTorchEnabled: Boolean,
    onTorch: () -> Unit,
    onFlip: () -> Unit,
) {
    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Companion.End)
    ) {
        IconButton(onTorch) {
            Icon(
                imageVector = ImageVector.Companion.vectorResource(
                    if (isTorchEnabled) R.drawable.ic_torch_on
                    else R.drawable.ic_torch_off
                ),
                contentDescription = "Torch on/off",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        IconButton(onFlip) {
            Icon(
                imageVector = ImageVector.Companion.vectorResource(R.drawable.ic_flip_camera),
                contentDescription = "Flip Camera",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}