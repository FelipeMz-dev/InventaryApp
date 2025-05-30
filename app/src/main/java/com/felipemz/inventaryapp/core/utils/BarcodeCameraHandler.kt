package com.felipemz.inventaryapp.core.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.MeteringPoint
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.felipemz.inventaryapp.R

class BarcodeCameraHandler(
    private val ctx: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val onBarcodeScanned: (String) -> Unit,
    private val onDismiss: () -> Unit
) {
    private var camera: Camera? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    val isTorchEnabled = mutableStateOf(false)

    private fun playBeep() {
        try {
            val mediaPlayer = MediaPlayer.create(ctx, R.raw.beep)
            mediaPlayer.setOnCompletionListener { it.release() }
            mediaPlayer.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @OptIn(ExperimentalGetImage::class)
    fun setupCamera(
        previewView: PreviewView,
        cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    ) {
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val barcodeScanner = BarcodeScanning.getClient()

                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                analysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        barcodeScanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    barcode.rawValue?.let {
                                        playBeep()
                                        onBarcodeScanned(it)
                                        cameraProvider.unbindAll()
                                        terminate()
                                        onDismiss()
                                    }
                                }
                            }
                            .addOnFailureListener {
                                it.printStackTrace()
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    } else {
                        imageProxy.close()
                    }
                }

                try {
                    cameraProvider.unbindAll()
                    camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        analysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx)
        )
    }

    fun setFocus(point: MeteringPoint) {
        camera?.let {
            val action = FocusMeteringAction.Builder(point).build()
            it.cameraControl.startFocusAndMetering(action)
        }
    }

    fun toggleTorch() {
        camera?.let {
            val enabled = it.cameraInfo.torchState.value == TorchState.ON
            it.cameraControl.enableTorch(!enabled)
            isTorchEnabled.value = !enabled
        }
    }

    fun terminate() {
        camera?.let {
            it.cameraControl.enableTorch(false)
            camera = null
        }
    }

    fun switchCamera(
        previewView: PreviewView,
        cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    ) {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else CameraSelector.DEFAULT_BACK_CAMERA
        setupCamera(previewView, cameraProviderFuture)
    }
}