package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun <T> SortableList(
    modifier: Modifier,
    items: List<T>,
    onMove: (from: Int, to: Int) -> Unit,
    itemContent: @Composable (item: T, isDragging: Boolean) -> Unit,
) {
    val draggingIndex = remember { mutableStateOf<Int?>(null) }
    val offsetY = remember { mutableFloatStateOf(0f) }

    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->

            var itemHeight by remember { mutableFloatStateOf(0f) }

            val isDragging = index == draggingIndex.value

            val modifier = if (isDragging) {
                Modifier
                    .offset { IntOffset(0, offsetY.floatValue.toInt()) }
                    .zIndex(1f)
            } else Modifier

            Box(
                modifier = modifier
                    .onGloballyPositioned { coordinates ->
                        itemHeight = coordinates.size.height.toFloat()
                    }
                    .pointerInput(Unit, items) {
                        detectDragAndDrop(
                            index = index,
                            draggingIndex = draggingIndex,
                            offsetY = offsetY,
                            itemHeight = itemHeight,
                            length = items.lastIndex,
                            onMove = onMove
                        )
                    }
            ) { itemContent(item, isDragging) }
        }
    }
}

suspend fun PointerInputScope.detectDragAndDrop(
    index: Int,
    draggingIndex: MutableState<Int?>,
    offsetY: MutableState<Float>,
    itemHeight: Float,
    length: Int,
    onMove: (Int, Int) -> Unit
) {
    detectDragGesturesAfterLongPress(
        onDragStart = {
            draggingIndex.value = index
        },
        onDragEnd = {
            draggingIndex.value = null
            offsetY.value = 0f
        },
        onDragCancel = {
            draggingIndex.value = null
            offsetY.value = 0f
        },
        onDrag = { change, dragAmount ->
            change.consume()
            offsetY.value += dragAmount.y

            val currentIndex = draggingIndex.value ?: return@detectDragGesturesAfterLongPress
            val targetIndex = (currentIndex + (offsetY.value / itemHeight).toInt()).coerceIn(0, length)

            if (targetIndex != currentIndex) {
                onMove(currentIndex, targetIndex)
                draggingIndex.value = targetIndex
                offsetY.value -= (offsetY.value / itemHeight).toInt() * itemHeight
            }
        }
    )
}