package com.felipemz.inventaryapp.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.ui.theme.InventaryAppTheme
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by inject()
    private var lastBackPressedTime: Long = 0

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
            else -> viewModel.eventHandler(event)
        }
    }

    override fun onBackPressed() {
        if (viewModel.state.value.isSearchFocused) {
            viewModel.eventHandler(HomeEvent.OnFocusSearch(false))
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastBackPressedTime < Toast.LENGTH_SHORT) {
                super.onBackPressed()
            } else {
                lastBackPressedTime = currentTime
                Toast.makeText(this, getString(R.string.copy_press_again_to_exit), Toast.LENGTH_SHORT).show()
            }
        }
    }
}