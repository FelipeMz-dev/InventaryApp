package com.felipemz.inventaryapp.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.KEY_PRODUCT_ID
import com.felipemz.inventaryapp.ui.product.ProductFormActivity
import com.felipemz.inventaryapp.ui.theme.InventaryAppTheme
import org.koin.android.ext.android.inject
import java.util.Date

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

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }

    private fun eventHandler(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnOpenCalendar -> {
                val date = Date()
                val datePicker = DatePickerDialog(
                    this,
                    { _: DatePicker?, year: Int, month: Int, day: Int ->

                    },
                    date.year,
                    date.month,
                    date.day
                )
                datePicker.show()
            }
            is HomeEvent.OnFAB -> startActivity(
                Intent(this, ProductFormActivity::class.java)
            )
            is HomeEvent.OnOpenProduct -> startActivity(
                Intent(this, ProductFormActivity::class.java).putExtras(
                    Bundle().apply { putInt(KEY_PRODUCT_ID, event.product.id) }
                )
            )
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