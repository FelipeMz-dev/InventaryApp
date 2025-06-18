package com.felipemz.inventaryapp.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.felipemz.inventaryapp.ui.home.tabs.HomeTabs

@Composable
internal fun BottomBarHome(
    tabSelected: MutableState<HomeTabs>,
) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        actions = {
            HomeTabs.entries.forEach { tab ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .imePadding()
                        .pointerInput(Unit) {
                            detectTapGestures { tabSelected.value = tab }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    Icon(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .then(
                                if (tab == tabSelected.value) Modifier.background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    shape = CircleShape
                                )
                                else Modifier
                            )
                            .padding(horizontal = 16.dp)
                            .aspectRatio(1f),
                        painter = painterResource(tab.icon),
                        tint = MaterialTheme.colorScheme.let { if (tab == tabSelected.value) it.primary else it.onSurface },
                        contentDescription = null
                    )

                    Text(
                        text = tab.tittle,
                        fontWeight = if (tab == tabSelected.value) FontWeight.Black else FontWeight.Normal,
                    )
                }
            }
        }
    )
}