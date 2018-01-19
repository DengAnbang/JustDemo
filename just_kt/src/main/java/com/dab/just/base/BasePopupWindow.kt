package com.dab.just.base

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import com.dab.just.R
import com.dab.just.custom.ProgressDialog
import com.dab.just.interfaces.RequestHelper
import com.dab.just.utlis.ToastUtils
import com.dab.just.utlis.kt.click
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by dab on 2018/1/4 0004 09:01
 */
abstract class BasePopupWindow(private val activity: Activity) : PopupWindow(activity), RequestHelper {

    private var mCompositeDisposable:CompositeDisposable? =null
    private var mProgressDialog : ProgressDialog?=null
    @LayoutRes
    abstract fun setContentRes(): Int

    @ColorInt
    open fun setBackgroundColor(): Int = Color.TRANSPARENT

    open fun setBackgroundAlpha(): Float = 0.7f
    /**
     * 是否点击外部消失
     */
    open fun setFocusable(): Boolean = true
    open fun setAnim() = R.style.AnimBottomToTop

    open fun initView(view: View) {
        click(contentView) {
            if (setFocusable()) {
                dismiss()
            }
        }
    }

    open fun initEvent() {}
    open fun addDisposable(disposable: Disposable) {
        mCompositeDisposable= mCompositeDisposable?: CompositeDisposable()
        mCompositeDisposable?.add(disposable)
    }


    //隐藏软键盘
    private fun hideSoftInput(activity: Activity) {
        activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (imm != null || imm is InputMethodManager) {
            val currentFocus = activity.currentFocus
            if (currentFocus != null) {
                imm as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus.windowToken, 0) //强制隐藏键盘}
            }
        }
    }
init {
    initThis()
}
    private fun initThis() {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val menuView = inflater.inflate(setContentRes(), null)
        //设置SelectPicPopupWindow的View
        this.contentView = menuView
        //设置SelectPicPopupWindow弹出窗体的宽
        this.width = ViewGroup.LayoutParams.MATCH_PARENT
        //设置SelectPicPopupWindow弹出窗体的高
        this.height = ViewGroup.LayoutParams.MATCH_PARENT
        //设置SelectPicPopupWindow弹出窗体可点击
        this.isFocusable = setFocusable()
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.animationStyle = setAnim()
        //实例化一个ColorDrawable颜色为全透明
//        ColorDrawable dw = new ColorDrawable(Constant.COLOR_CONFIRM_DELETE_POPUP_WINDOW);
        val dw = ColorDrawable(setBackgroundColor())
//        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw)


        hideSoftInput(activity)
        changeWindowAlpha(activity.window, setBackgroundAlpha())
        initView(contentView)
        initEvent()
    }

    private fun changeWindowAlpha(window: Window?, alpha: Float) {
        if (window == null) {
            Log.e(TAG, "changeWindowAlpha: window == null")
            return
        }
        val lp = window.attributes
        lp.alpha = alpha
        window.attributes = lp
    }

    override fun dismiss() {
        super.dismiss()
        changeWindowAlpha(activity.window, 1f)
        mCompositeDisposable?.dispose()
    }

    override fun showToast(msg: String?) {
        ToastUtils.showToast(msg)
    }

    override fun showLoadDialog(msg: String, canCancel: Boolean) {
        if (!isShowing) return
        mProgressDialog=mProgressDialog?: ProgressDialog(activity, R.style.Theme_ProgressDialog)
        mProgressDialog?.let {
            it.setCanceledOnTouchOutside(canCancel)
            it.setMessage(msg)
            if (!it.isShowing) { it.show() }
        }
    }

    override fun dismissLoadDialog() {
        mProgressDialog?.dismiss()
    }

    override fun cancelRequest(disposable: Disposable) {
        addDisposable(disposable)
    }
}