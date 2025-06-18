package com.felipemz.inventaryapp.ui.product_form

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.felipemz.inventaryapp.core.KEY_CATEGORY_CHANGED
import com.felipemz.inventaryapp.core.KEY_CATEGORY_TO_CHANGE
import com.felipemz.inventaryapp.core.KEY_PRODUCT_ID
import com.felipemz.inventaryapp.core.extensions.showToast
import com.felipemz.inventaryapp.ui.theme.InventaryAppTheme
import org.koin.android.ext.android.inject

class ProductFormActivity : AppCompatActivity() {

    private val viewModel: ProductFormViewModel by inject()

    private lateinit var productFormLauncher: ActivityResultLauncher<Intent>

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

        handleResult()
        initViewModel()
    }

    private fun initViewModel() {
        val bundle = intent.extras
        val productId = bundle?.getInt(KEY_PRODUCT_ID)
        val categoryId = bundle?.getInt(KEY_CATEGORY_TO_CHANGE)
        categoryId?.let { if (it != 0) eventHandler(ProductFormEvent.SetCategoryToChange(it)) }
        eventHandler(ProductFormEvent.Init(productId))
        viewModel.actionLiveData.observe(this) { action ->
            action?.also { actionHandler(it) }
        }
    }

    private fun eventHandler(event: ProductFormEvent) {
        when (event) {
            is ProductFormEvent.OnBack -> finish()
            is ProductFormEvent.GoToChangeCategory -> {
                goToChangeCategoryFromProduct(
                    productId = event.productId,
                    categoryId = event.categoryId
                )
            }
            else -> viewModel.eventHandler(event)
        }
    }

    private fun actionHandler(action: ProductFormAction) {
        when (action) {
            is ProductFormAction.ShowMessage -> showToast(action.message)
            is ProductFormAction.OnCategoryChangeDone -> onCategoryChangeDone(action.productId)
            else -> Unit
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

    private fun onCategoryChangeDone(productId: Int) {
        setResult(
            RESULT_OK, Intent().apply {
                putExtra(KEY_CATEGORY_CHANGED, productId)
            }
        )
        finish()
    }

    private fun goToChangeCategoryFromProduct(
        productId: Int,
        categoryId: Int
    ) {
        val intent = Intent(this, ProductFormActivity::class.java).apply {
            putExtra(KEY_PRODUCT_ID, productId)
            putExtra(KEY_CATEGORY_TO_CHANGE, categoryId)
        }
        productFormLauncher.launch(intent)
    }

    private fun handleResult() {
        productFormLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    val productId = data.getIntExtra(KEY_CATEGORY_CHANGED, 0)
                    if (productId != 0) {
                        viewModel.eventHandler(ProductFormEvent.SetChangedSuccessfulCategory(productId))
                    }
                }
            }
        }
    }
}