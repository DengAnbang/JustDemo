package com.dab.just.interfaces

import io.reactivex.disposables.Disposable

/**
 * Created by dab on 2017/11/8 0008 16:08
 */

interface RequestHelper {
    /**
     * 显示消息提示
     *
     * @param msg
     */
    fun showToast(msg: String?)

    /**
     * 显示加载的动画
     */
    fun showLoadDialog(msg: String = "加载中...", canCancel: Boolean = true)

    /**
     * 取消加载的动画
     */
    fun dismissLoadDialog()

    /**
     * 取消请求
     *
     * @param disposable
     */
    fun cancelRequest(disposable: Disposable)

}
