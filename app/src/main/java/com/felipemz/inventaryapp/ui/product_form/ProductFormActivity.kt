package com.felipemz.inventaryapp.ui.product_form

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.felipemz.inventaryapp.core.KEY_PRODUCT_ID
import com.felipemz.inventaryapp.ui.theme.InventaryAppTheme
import org.koin.android.ext.android.inject

class ProductFormActivity : AppCompatActivity() {

    private val viewModel: ProductFormViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            InventaryAppTheme {
                ProductFormScreen(
                    state = state,
                    eventHandler = ::eventHandler
                )
            }
        }

        initViewModel()

        setShowWhenLocked()
    }

    private fun initViewModel() {
        val bundle = intent.extras
        val productId = bundle?.getInt(KEY_PRODUCT_ID) ?: 0
        eventHandler(ProductFormEvent.Init(productId))
    }

    private fun eventHandler(event: ProductFormEvent) {
        when (event) {
            is ProductFormEvent.OnBack -> finish()
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