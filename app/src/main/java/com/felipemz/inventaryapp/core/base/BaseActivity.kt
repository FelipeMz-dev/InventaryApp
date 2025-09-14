package com.felipemz.inventaryapp.core.base

import android.content.Context
import android.content.Intent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.felipemz.inventaryapp.ui.product_form.ProductFormActivity

open class BaseActivity: AppCompatActivity() {

    companion object {
        fun getIntent(context: Context, productId: String, categoryId: String): Intent {
            val intent = Intent(context, ProductFormActivity::class.java)
            intent.putExtra("PRODUCT_ID", productId)
            intent.putExtra("CATEGORY_ID", categoryId)
            return intent
        }
    }

    open fun setShowWhenLocked() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }
}