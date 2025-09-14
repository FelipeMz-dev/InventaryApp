package com.felipemz.inventaryapp.ui.product_form

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.felipemz.inventaryapp.core.KEY_CATEGORY_CHANGED
import com.felipemz.inventaryapp.core.KEY_CATEGORY_TO_CHANGE
import com.felipemz.inventaryapp.core.KEY_CREATE_FROM_BARCODE
import com.felipemz.inventaryapp.core.KEY_CREATE_FROM_CATEGORY
import com.felipemz.inventaryapp.core.KEY_PACKAGE_CHANGE
import com.felipemz.inventaryapp.core.KEY_PACKAGE_TO_CHANGE
import com.felipemz.inventaryapp.core.KEY_PRODUCT_ID
import com.felipemz.inventaryapp.core.extensions.showToast
import com.felipemz.inventaryapp.ui.LocalProductList
import com.felipemz.inventaryapp.ui.theme.InventaryAppTheme
import org.koin.android.ext.android.inject

class ProductFormActivity : AppCompatActivity() {
    private val viewModel: ProductFormViewModel by inject()
    private val productFormLauncher = makeResultActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val productList by viewModel.productList.collectAsStateWithLifecycle()
            InventaryAppTheme {
                CompositionLocalProvider(LocalProductList provides productList) {
                    ProductFormScreen(
                        state = state,
                        eventHandler = ::eventHandler
                    )
                }
            }
        }

        initViewModel()
    }

    private fun initViewModel() {
        val bundle = intent.extras
        val productId = bundle?.getInt(KEY_PRODUCT_ID)
        val categoryId = bundle?.getInt(KEY_CATEGORY_TO_CHANGE)
        val packageId = bundle?.getInt(KEY_PACKAGE_TO_CHANGE)
        val fromBarcode = bundle?.getString(KEY_CREATE_FROM_BARCODE)
        val fromCategory = bundle?.getInt(KEY_CREATE_FROM_CATEGORY)
        categoryId?.let { if (it != 0) eventHandler(ProductFormEvent.SetCategoryToChange(it)) }
        packageId?.let { if (it != 0) eventHandler(ProductFormEvent.SetPackageToChange(it)) }
        eventHandler(
            ProductFormEvent.Init(
                productId = productId,
                barcode = fromBarcode,
                categoryId = fromCategory,
            )
        )
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
            is ProductFormEvent.GoToChangePackage -> {
                viewModel.state.value.editProduct?.let {
                    goToChangePackageFromProduct(
                        packageId = event.productId,
                        productId = it.id
                    )
                }
            }
            else -> viewModel.eventHandler(event)
        }
    }

    private fun actionHandler(action: ProductFormAction) {
        when (action) {
            is ProductFormAction.ShowMessage -> showToast(action.message)
            is ProductFormAction.OnCategoryChangeDone -> onCategoryChangeDone(action.productId)
            is ProductFormAction.OnPackageChangeDone -> onPackageChangeDone(action.productId)
            is ProductFormAction.OnCreateFromBarcode -> onCreateFromBarcode(action.barcode)
            else -> Unit
        }
    }

    private fun onCategoryChangeDone(productId: Int) {
        setResult(
            RESULT_OK, Intent().apply {
                putExtra(KEY_CATEGORY_CHANGED, productId)
            }
        )
        finish()
    }

    private fun onPackageChangeDone(productId: Int) {
        setResult(
            RESULT_OK, Intent().apply {
                putExtra(KEY_PACKAGE_CHANGE, productId)
            }
        )
        finish()
    }

    private fun onCreateFromBarcode(barcode: String) {
        setResult(
            RESULT_OK, Intent().apply {
                putExtra(KEY_CREATE_FROM_BARCODE, barcode)
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

    private fun goToChangePackageFromProduct(
        packageId: Int,
        productId: Int,
    ) {
        val intent = Intent(this, ProductFormActivity::class.java).apply {
            putExtra(KEY_PRODUCT_ID, packageId)
            putExtra(KEY_PACKAGE_TO_CHANGE, productId)
        }
        productFormLauncher.launch(intent)
    }

    private fun makeResultActivity() = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                val productId = data.getIntExtra(KEY_CATEGORY_CHANGED, 0)
                if (productId != 0) {
                    viewModel.eventHandler(ProductFormEvent.SetChangedSuccessfulCategory(productId))
                }
                viewModel.state.value.editProduct?.let {
                    val packageId = data.getIntExtra(KEY_PACKAGE_CHANGE, 0)
                    if (packageId != 0) {
                        viewModel.eventHandler(ProductFormEvent.SetChangedSuccessfulPackage(packageId))
                    }
                }
            }
        }
    }
}