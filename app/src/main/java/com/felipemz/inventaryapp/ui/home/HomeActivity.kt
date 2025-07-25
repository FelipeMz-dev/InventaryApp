package com.felipemz.inventaryapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.felipemz.inventaryapp.R
import com.felipemz.inventaryapp.core.KEY_CREATE_FROM_BARCODE
import com.felipemz.inventaryapp.core.KEY_CREATE_FROM_CATEGORY
import com.felipemz.inventaryapp.core.KEY_PRODUCT_ID
import com.felipemz.inventaryapp.core.extensions.showToast
import com.felipemz.inventaryapp.ui.LocalProductList
import com.felipemz.inventaryapp.ui.home.HomeEvent.Init
import com.felipemz.inventaryapp.ui.home.tabs.HomeTabs
import com.felipemz.inventaryapp.ui.movements.MovementsActivity
import com.felipemz.inventaryapp.ui.product_form.ProductFormActivity
import com.felipemz.inventaryapp.ui.theme.InventaryAppTheme
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by inject()
    private var lastBackPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val productList by viewModel.productList.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) { eventHandler(Init) }

            InventaryAppTheme {
                CompositionLocalProvider(LocalProductList provides productList) {
                    HomeScreen(
                        state = state,
                        eventHandler = ::eventHandler
                    )
                }
            }
        }

        viewModel.actionLiveData.observe(this) { action ->
            action?.also { actionHandler(it) }
        }
    }

    private fun eventHandler(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnFAB -> when (event.tabSelected) {
                HomeTabs.PRODUCTS -> startProductForm()
                HomeTabs.MOVEMENTS -> startActivity(Intent(this, MovementsActivity::class.java))
                else -> Unit
            }
            is HomeEvent.OnOpenProduct -> startProductForm(productId = event.product.id, categoryId = null)
            else -> viewModel.eventHandler(event)
        }
    }

    private fun actionHandler(action: HomeAction) {
        when (action) {
            is HomeAction.OnBack -> finish()
            is HomeAction.ShowMessage -> showToast(action.message)
            is HomeAction.CreateProductFromBarcode -> startProductForm(barcode = action.barcode)
            is HomeAction.OpenProduct -> startProductForm(productId = action.productId, categoryId = null)
        }
    }

    private fun startProductForm(
        productId: Int? = null,
        categoryId: Int? = viewModel.state.value.categorySelected?.id,
        barcode: String? = null
    ){
        startActivity(
            Intent(this, ProductFormActivity::class.java).putExtras(
                Bundle().apply {
                    productId?.let { putInt(KEY_PRODUCT_ID, it) }
                    categoryId?.let { putInt(KEY_CREATE_FROM_CATEGORY, it) }
                    barcode?.let { putString(KEY_CREATE_FROM_BARCODE, it) }
                }
            )
        )
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