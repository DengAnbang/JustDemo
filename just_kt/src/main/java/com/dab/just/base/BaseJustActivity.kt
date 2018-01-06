package com.dab.just.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.dab.just.R
import com.dab.just.custom.TitleBar

import com.dab.just.utlis.extend.visibility
import org.jetbrains.anko.find

/**
 * Created by dab on 2017/12/30 0030 15:12
 */
abstract class BaseJustActivity : AppCompatActivity() {
    @LayoutRes
    abstract fun setContentViewRes(): Int

    open val fullScreen = false
    private val rootLayout by lazy {
        find<LinearLayout>(R.id.root_layout)
    }
    val titleBar by lazy {
        find<TitleBar>(R.id.titleBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_layout_base)
        if (setContentViewRes() < 0) return
        layoutInflater.inflate(setContentViewRes(), rootLayout, true)
        val statusBar = find<View>(R.id.view_status_bar)
        initStatusBar(statusBar)
        setStatusBar(statusBar)
        initView()
        initEvent()
        initData()

    }

    open fun setStatusBar(statusBar: View) {}
    open fun initEvent() {}
    open fun initData() {}
    open fun initView() {}
    private fun initStatusBar(view: View) {
        var statusBarHeight = 0
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            //获取status_bar_height资源的ID
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = resources.getDimensionPixelSize(resourceId)
            }
        }
        val layoutParams = view.layoutParams
        layoutParams.height = statusBarHeight
        view.layoutParams = layoutParams
        visibility(view, !fullScreen)
    }

    override fun setTitle(title: CharSequence?) {
        titleBar.titleView.setText(title)
    }
}