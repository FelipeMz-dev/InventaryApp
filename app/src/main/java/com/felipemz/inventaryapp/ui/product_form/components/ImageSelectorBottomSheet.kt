package com.felipemz.inventaryapp.ui.product_form.components

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.extensions.hasCameraPermission
import com.felipemz.inventaryapp.core.utils.BitmapContentManager
import com.felipemz.inventaryapp.core.utils.BitmapUtil.dragBitmap
import com.felipemz.inventaryapp.core.utils.BitmapUtil.rotateBitmap
import com.felipemz.inventaryapp.core.utils.BitmapUtil.scaleBitmapFromOffset
import com.felipemz.inventaryapp.core.utils.BitmapUtil.setAspectRatio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ImageSelectorBottomSheet(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {

    val context = LocalContext.current
    var image by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageScaled by remember(image) {
        mutableStateOf(image?.let { setAspectRatio(it, 1.0f) })
    }
    var position by remember { mutableStateOf(Offset.Zero) }
    val sheetState = rememberModalBottomSheetState()

    val bitmapManager = BitmapContentManager(context)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { image = bitmapManager.getBitmapFromUri(it) }
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            imageUri?.let { image = bitmapManager.getBitmapFromUri(it) }
            imageUri = null
        }
    }
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imageUri = bitmapManager.createImageUri()
            imageUri?.let { cameraLauncher.launch(it) }
        }
    }

    LaunchedEffect(position) {
        image?.let {
            imageScaled = dragBitmap(
                bitmap = imageScaled ?: it,
                parentBitmap = it,
                offset = IntOffset(position.x.toInt(), position.y.toInt())
            ) { change -> position = Offset(change.x.toFloat(), change.y.toFloat()) }
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = stringResource(R.string.copy_select_dots),
                fontWeight = FontWeight.Bold
            )

            imageScaled?.let { selected ->

                LaunchedEffect(Unit) { sheetState.expand() }

                ImageWithCircleFocus(
                    modifier = Modifier.fillMaxWidth(),
                    image = selected,
                    onDrag = { offset -> position += offset }
                )

                ActionsEditImage(
                    modifier = Modifier.fillMaxWidth(),
                    onRotate = { image = image?.let { rotateBitmap(it) } },
                    onScale = { scale ->
                        image?.let { current ->
                            imageScaled = scaleBitmapFromOffset(
                                bitmap = current,
                                scale = scale,
                                aspectRatio = 1f,
                                offset = IntOffset(
                                    x = position.x.toInt(),
                                    y = position.y.toInt()
                                )
                            ) { position = Offset(it.x.toFloat(), it.y.toFloat()) }
                        }
                    }
                )

                BottomActions(
                    modifier = Modifier.fillMaxWidth(),
                    onRetry = { image = null },
                    onAccept = { onSelect(bitmapManager.saveBitmapToUri(selected).path.orEmpty()) }
                )
            } ?: ActionsImageObtain(
                modifier = Modifier.fillMaxWidth(),
                onGallery = { galleryLauncher.launch("image/*") },
                onCamera = {
                    if (context.hasCameraPermission()) {
                        imageUri = bitmapManager.createImageUri()
                        imageUri?.let { cameraLauncher.launch(it) }
                    } else {
                        requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionsEditImage(
    modifier: Modifier,
    onRotate: () -> Unit,
    onScale: (Float) -> Unit,
) {

    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Slider(
            modifier = Modifier.weight(1f),
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onScale(1f - it)
            },
            thumb = {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(4.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_resize),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )

        IconButton(onClick = {
            sliderPosition = 0f
            onRotate()
        }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_rotation),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ActionsImageObtain(
    modifier: Modifier,
    onGallery: () -> Unit,
    onCamera: () -> Unit,
) = Row(modifier = modifier) {

    ItemImageButton(
        modifier = Modifier.weight(1f),
        text = stringResource(R.string.copy_search_image),
        iconRes = R.drawable.ic_gallery,
        action = onGallery
    )

    ItemImageButton(
        modifier = Modifier.weight(1f),
        text = stringResource(R.string.copy_take_photo),
        iconRes = R.drawable.ic_camera,
        action = onCamera
    )
}

@Composable
private fun ImageWithCircleFocus(
    modifier: Modifier,
    image: Bitmap,
    onDrag: (Offset) -> Unit,
) = Box(modifier = modifier.clip(RoundedCornerShape(12.dp))) {

    Image(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    onDrag(change.previousPosition - change.position)
                }
            }
            .alpha(0.5f),
        bitmap = image.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Image(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(CircleShape),
        bitmap = image.asImageBitmap(),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun BottomActions(
    modifier: Modifier,
    onRetry: () -> Unit,
    onAccept: () -> Unit,
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {

    Button(
        modifier = Modifier.weight(1f),
        onClick = onRetry
    ) { Text(text = stringResource(R.string.copy_retry)) }

    Button(
        modifier = Modifier.weight(1f),
        onClick = onAccept
    ) { Text(text = stringResource(R.string.copy_accept)) }
}

@Composable
private fun ItemImageButton(
    modifier: Modifier,
    text: String,
    iconRes: Int,
    action: () -> Unit,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(4.dp)
) {

    Icon(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { action() }
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp)
            .size(48.dp),
        imageVector = ImageVector.vectorResource(iconRes),
        tint = MaterialTheme.colorScheme.primary,
        contentDescription = null
    )

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.outline
    )
}