package com.felipemz.inventaryapp.ui.movements

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.felipemz.inventaryapp.core.KEY_BARCODE_CREATE
import com.felipemz.inventaryapp.core.base.BaseActivity
import com.felipemz.inventaryapp.core.extensions.showToast
import com.felipemz.inventaryapp.ui.LocalProductList
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.Init
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnBack
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnClearBarcodeError
import com.felipemz.inventaryapp.ui.movements.MovementsEvent.OnSelectProductFromBarcode
import com.felipemz.inventaryapp.ui.product_form.ProductFormActivity
import com.felipemz.inventaryapp.ui.theme.InventaryAppTheme
import org.koin.android.ext.android.inject

class MovementsActivity : BaseActivity() {

    private val viewModel: MovementsViewModel by inject()
    private lateinit var movementsLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val productList by viewModel.productList.collectAsStateWithLifecycle()
            InventaryAppTheme {
                CompositionLocalProvider(LocalProductList provides productList) {
                    MovementsScreen(
                        state = state,
                        eventHandler = ::eventHandler
                    )
                }
            }
        }

        setShowWhenLocked()
        handleResult()
        initViewModel()
    }

    //private fun <T> LiveData<T>.observeAsState(): T? {
    //    var value: T? = null
    //    observe(this@MovementsActivity) { newValue ->
    //        value = newValue
    //    }
    //    return value
    //}

    private fun initViewModel() {
        viewModel.actionLiveData.observe(this) { action ->
            action?.also { actionHandler(it) }
        }
        viewModel.eventHandler(Init)
    }

    private fun eventHandler(event: MovementsEvent) {
        when (event) {
            is OnBack -> finish()
            else -> viewModel.eventHandler(event)
        }
    }

    private fun actionHandler(action: MovementsAction) {
        when (action) {
            is MovementsAction.CreateProductFromBarcode -> {
                val intent = Intent(this, ProductFormActivity::class.java).apply {
                    putExtra(KEY_BARCODE_CREATE, action.barcode)
                }
                movementsLauncher.launch(intent)
            }
            is MovementsAction.ShowMessage -> showToast(action.message)
            else -> Unit
        }
    }

    private fun handleResult() {
        movementsLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    val barcode = data.getStringExtra(KEY_BARCODE_CREATE)
                    barcode?.let {
                        viewModel.eventHandler(OnSelectProductFromBarcode(it))
                        viewModel.eventHandler(OnClearBarcodeError)
                    }
                }
            }
        }
    }
}