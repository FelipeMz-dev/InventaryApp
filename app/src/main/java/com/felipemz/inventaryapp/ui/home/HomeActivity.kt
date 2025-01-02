package com.felipemz.inventaryapp.ui.home

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.felipemz.inventaryapp.core.enums.HomeTabs
import com.felipemz.inventaryapp.core.extensions.ifTrue
import com.felipemz.inventaryapp.ui.theme.InventaryAppTheme
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            InventaryAppTheme {
                HomeScreen(
                    state = state,
                    eventHandler = ::eventHandler
                )
            }
        }
    }

    private fun eventHandler(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnCategorySelected -> {}
            else -> viewModel.eventHandler(event)
        }
    }
}