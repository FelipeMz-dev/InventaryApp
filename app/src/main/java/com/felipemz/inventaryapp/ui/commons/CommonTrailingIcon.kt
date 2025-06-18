package com.felipemz.inventaryapp.ui.commons

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.felipemz.inventaryapp.R
import java.util.Locale

@Composable
internal fun CommonTrailingIcon(
    isTextEmpty: Boolean,
    onEdit: (String) -> Unit,
) {

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        matches?.firstOrNull()?.let { onEdit(it.replaceFirstChar { char -> char.uppercaseChar() }) }
    }

    IconButton(
        onClick = {
            if (isTextEmpty) launcher.launch(intent)
            else onEdit(String())
        }
    ) {
        Icon(
            imageVector = if (!isTextEmpty) Icons.Rounded.Clear
            else ImageVector.vectorResource(id = R.drawable.ic_mic),
            tint = MaterialTheme.colorScheme.outline,
            contentDescription = null
        )
    }
}

@Composable
fun startVoiceInput(
    context: Context,
    onResult: (String?) -> Unit
) {
    val activity = context as? Activity ?: return

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        onResult(matches?.firstOrNull())
    }

    launcher.launch(intent)
}