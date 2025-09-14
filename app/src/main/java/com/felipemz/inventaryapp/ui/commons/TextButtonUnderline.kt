package com.felipemz.inventaryapp.ui.commons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun TextButtonUnderline(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) = Text(
    modifier = modifier
        .clip(CircleShape)
        .clickable(enabled = isEnabled) { onClick() }
        .padding(8.dp),
    text = text,
    color = MaterialTheme.colorScheme.primary.let { color ->
        color.takeIf { isEnabled } ?: color.copy(alpha = 0.4f)
    },
    style = MaterialTheme.typography.bodyMedium,
    textDecoration = TextDecoration.Underline,
    fontWeight = FontWeight.Black,
    textAlign = TextAlign.Center,
)