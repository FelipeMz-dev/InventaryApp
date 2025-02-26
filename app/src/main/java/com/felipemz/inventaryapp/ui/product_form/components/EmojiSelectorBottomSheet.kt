package com.felipemz.inventaryapp.ui.product_form.components

import android.content.res.Resources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felipemz.inventaryapp.R
import kotlinx.coroutines.launch

private fun Resources.getEmojiList() = listOf(
    getStringArray(R.array.string_food).asList() to getStringArray(R.array.emoji_food).asList(),
    getStringArray(R.array.string_object).asList() to getStringArray(R.array.emoji_object).asList(),
    getStringArray(R.array.string_nature).asList() to getStringArray(R.array.emoji_nature).asList(),
    getStringArray(R.array.string_travel).asList() to getStringArray(R.array.emoji_travel).asList(),
    getStringArray(R.array.string_activity).asList() to getStringArray(R.array.emoji_activity).asList(),
)

private fun Resources.getTitleList() = getStringArray(R.array.emoji_title).asList()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmojiSelectorBottomSheet(
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit,
) {

    val resources = LocalContext.current.resources
    val emojiList by remember { derivedStateOf { resources.getEmojiList() } }
    val emojiTitles by remember { derivedStateOf { resources.getTitleList() } }
    val pagerState = rememberPagerState(0) { emojiList.size }
    val scope = rememberCoroutineScope()

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth()) {

            TabsEmojiTitle(
                modifier = Modifier.fillMaxWidth(),
                selected = pagerState.currentPage,
                emojiTitles = emojiTitles
            ) {
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }

            PagerEmojiSelector(
                modifier = Modifier.padding(8.dp),
                pagerState = pagerState,
                emojiList = emojiList,
                onSelect = onSelect
            )
        }
    }
}

@Composable
private fun PagerEmojiSelector(
    modifier: Modifier,
    pagerState: PagerState,
    emojiList: List<Pair<List<String>, List<String>>>,
    onSelect: (String) -> Unit
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        verticalAlignment = Alignment.Top
    ) { page ->

        val emojisPage by remember { derivedStateOf { emojiList.getOrNull(page) } }

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            emojisPage?.let {
                it.second.forEachIndexed { index, emojis ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {

                        Text(
                            text = it.first.getOrNull(index).orEmpty(),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline,
                        )

                        EmojiSelector(
                            modifier = Modifier.fillMaxWidth(),
                            emojis = emojis,
                            onSelect = onSelect
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun EmojiSelector(
    modifier: Modifier,
    emojis: String,
    onSelect: (String) -> Unit
) {
    FlowRow(modifier = modifier) {
        emojis.split(" ").forEach { emoji ->
            Text(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onSelect(emoji) }
                    .padding(2.dp),
                text = emoji,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
private fun TabsEmojiTitle(
    modifier: Modifier,
    selected: Int,
    emojiTitles: List<String>,
    onSelect: (Int) -> Unit,
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = selected
    ) {
        emojiTitles.forEachIndexed { index, title ->
            Tab(
                text = {
                    Text(
                        text = title,
                        fontSize = 24.sp
                    )
                },
                selected = index == selected,
                onClick = { onSelect(index) }
            )
        }
    }
}