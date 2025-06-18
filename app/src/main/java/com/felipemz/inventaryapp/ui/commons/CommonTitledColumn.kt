package com.felipemz.inventaryapp.ui.commons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.felipemz.inventaryapp.core.extensions.ifTrue

@Composable
internal fun CommonFormField(
    modifier: Modifier,
    title: String,
    concealable: Boolean = false,
    isMandatory: Boolean? = null,
    visible: Boolean = true,
    thumbContent: (@Composable () -> Unit)? = null,
    onOpen: (suspend () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {

    var visibility by remember(visible) { mutableStateOf(visible) }

    LaunchedEffect(key1 = visibility) {
        visibility.ifTrue { onOpen?.invoke() }
    }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.weight(1f),
                text = when (isMandatory) {
                    true -> " *"
                    false -> " (Opcional)"
                    else -> String()
                },
                color = if (isMandatory == true) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.outline
            )

            concealable.ifTrue {
                thumbContent?.let { it() } ?: DropDownIcon(
                    visibility = visibility,
                    onVisibilityChange = { visibility = it }
                )
            }
        }

        if (!concealable && visibility) Column { content() }
        else AnimatedVisibility(visible = visibility) {
            LaunchedEffect(transition.currentState) {
                (transition.currentState == EnterExitState.Visible).ifTrue { onOpen?.invoke() }
            }
            Column { content() }
        }
    }
}

@Composable
private fun DropDownIcon(
    visibility: Boolean,
    onVisibilityChange: (Boolean) -> Unit
) {
    val iconDrop by remember(visibility) {
        derivedStateOf {
            if (visibility) Icons.Rounded.KeyboardArrowUp
            else Icons.Rounded.KeyboardArrowDown
        }
    }

    IconButton(
        onClick = { onVisibilityChange(!visibility) }
    ) {
        Icon(
            imageVector = iconDrop,
            contentDescription = null
        )
    }
}