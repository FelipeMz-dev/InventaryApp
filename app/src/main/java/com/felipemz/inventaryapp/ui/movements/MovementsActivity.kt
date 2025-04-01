package com.felipemz.inventaryapp.ui.movements

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.felipemz.inventaryapp.ui.product_form.ProductFormEvent
import com.felipemz.inventaryapp.ui.product_form.ProductFormScreen
import com.felipemz.inventaryapp.ui.product_form.ProductFormViewModel
import com.felipemz.inventaryapp.ui.theme.InventaryAppTheme
import org.koin.android.ext.android.inject

class MovementsActivity : AppCompatActivity() {

    private val viewModel: MovementsViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setShowWhenLocked()
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            InventaryAppTheme {
                MovementsScreen(
                    state = state,
                    eventHandler = ::eventHandler
                )
            }
        }
    }

    private fun eventHandler(event: MovementsEvent) {
        when (event) {
            is MovementsEvent.OnBack -> finish()
            else -> viewModel.eventHandler(event)
        }
    }

    private fun setShowWhenLocked() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }
}