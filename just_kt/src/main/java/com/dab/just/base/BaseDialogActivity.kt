package com.dab.just.base

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.dab.just.R
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

/**
 *基础dialog式activity。子activity在manifest注册时需要设置为Dialog，或类似的theme。
 */
abstract open class BaseDialogActivity : BaseJustActivity() {

    open fun exitAnim(): Int = R.anim.fade_out
    override fun beforeSetContentView() {
        super.beforeSetContentView()
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setFinishOnTouchOutside(true)
        }
        fullScreen = true
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        onLayout()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        onLayout()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        onLayout()
    }

    open fun onLayout() {
        window.setLayout(matchParent, wrapContent)
    }

    override fun finish() {
        super.finish()
        val exit = exitAnim()
        if (exit > 0) {
            overridePendingTransition(0, exit)
        }
    }
}