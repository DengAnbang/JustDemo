package com.dab.just.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.dab.just.R
import com.dab.just.utlis.ToastUtils
import com.dab.just.utlis.extend.click
import com.dab.just.utlis.extend.setText
import com.dab.just.utlis.extend.visibility
import org.jetbrains.anko.find

/**
 * Created by dab on 2017/12/30 0030 15:12
 * 基础的Activity,拥有titleBar
 */
abstract class BaseJustActivity : AppCompatActivity() {
    @LayoutRes
    abstract fun setContentViewRes(): Int

    open var fullScreen = false
    private val rootLayout by lazy {
        find<LinearLayout>(R.id.root_layout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        setContentView(R.layout.activity_layout_base)
        if (setContentViewRes() <= 0) return
        layoutInflater.inflate(setContentViewRes(), rootLayout, true)
        val statusBar = find<View>(R.id.view_status_bar)
        initStatusBar(statusBar)
        setStatusBar(statusBar)
        initView()
        initEvent()
        initData()
        visibility(find<View>(R.id.layout_title_bar), !fullScreen)
    }

    open fun showToast(msg: String?) {
        ToastUtils.showToast(this, msg)
    }

    open fun beforeSetContentView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    open fun setStatusBar(statusBar: View) {}

    open fun initEvent() {}
    open fun initData() {}
    open fun initView() {
        click(R.id.tv_base_back) { onBackPressed()}
    }

    /**
     * 设置title
     */
    override fun setTitle(title: CharSequence?) {
        setText(R.id.tv_base_title,title)
    }

    /**
     * 设置右边的按钮
     */
    fun setRightText(msg: CharSequence?,onClick:(View)->Unit):TextView{
        R.id.tv_base_btn.let {
            click(it,onClick)
            visibility(it,true)
            setText(it,msg)
        }
        return find(R.id.tv_base_btn)
    }
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


}