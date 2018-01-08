package com.dab.just.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dab.just.R
import com.dab.just.custom.ProgressDialog
import com.dab.just.interfaces.RequestHelper
import com.dab.just.utlis.ToastUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by dab on 2017/8/19 0019 15:41
 * 懒加载Fragment,并且实现请求接口RequestHelper
 */

abstract class LazyFragment : Fragment(), RequestHelper {
    open var mActivity: Activity? = null
    private var isPrepared: Boolean = false
    private var isVisibleToMe = false//自己是否用户可见
    private var hidden: Boolean = false//自己当前的状态是否是隐藏了的
    open fun onVisibleToUser() {
        onVisibleOrInvisibleToUser(true)
    }

    open fun onInvisibleToUser() {
        onVisibleOrInvisibleToUser(false)
    }

    open fun onVisibleOrInvisibleToUser(visible: Boolean) {}
    protected abstract fun onFirstVisibleToUser(view: View?)
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Activity) {
            mActivity = context
        }
    }

    private fun onVisible() {
        if (isVisibleToMe) return
        if (isPrepared) {
            onVisibleToUser()
        } else {
            onFirstVisibleToUser(view)
            isPrepared = true
        }
        isVisibleToMe = true
    }


    private fun invisible() {
        if (!isVisibleToMe) return
        onInvisibleToUser()
        isVisibleToMe = false
    }

    @LayoutRes
    protected abstract fun viewLayoutID(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (viewLayoutID() != 0) {
            inflater.inflate(viewLayoutID(), null)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hidden) {
            onVisible()
        }
    }

    override fun onPause() {
        super.onPause()
        if (!hidden) {
            invisible()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        this.hidden = hidden
        if (hidden) {
            invisible()
        } else {
            onVisible()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            onVisible()
        } else {
            invisible()
        }
    }

    private var mProgressDialog: ProgressDialog? = null

    private val mCompositeDisposable by lazy { CompositeDisposable() }
    override fun cancelRequest(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun showToast(msg: String?) {
        ToastUtils.showToast(msg)
    }

    override fun showLoadDialog(msg: String, canCancel: Boolean) {
        if (!isVisibleToMe) return
        if (mProgressDialog == null)
            mProgressDialog = ProgressDialog(context!!, R.style.Theme_ProgressDialog)
        mProgressDialog!!.setCanceledOnTouchOutside(false)
        mProgressDialog!!.setMessage(msg)
        if (!mProgressDialog!!.isShowing) {
            mProgressDialog!!.show()
        }
    }


    override fun dismissLoadDialog() {
        mProgressDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}
