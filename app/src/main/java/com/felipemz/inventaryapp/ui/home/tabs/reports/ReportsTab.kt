package com.felipemz.inventaryapp.ui.home.tabs.reports

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun ReportsTab() {
    Text(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(width = 1.dp, shape = RoundedCornerShape(12.dp), color = Color.Gray),
        text = "Reports Tab"
    )
}