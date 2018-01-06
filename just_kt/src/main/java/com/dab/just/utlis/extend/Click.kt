package com.dab.just.utlis.extend

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import java.util.concurrent.TimeUnit

/**
 * Created by dab on 2017/11/10 0010 09:13
 */
inline fun Activity.click(@IdRes id: Int, crossinline onClick: (View) -> Unit) {
    val view: View = this.findViewById(id)
    click(view, onClick)
}

inline fun click(view: View, crossinline onClick: (View) -> Unit) {
    view.isEnabled = true
    RxView.clicks(view)
            .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
            .subscribe { onClick.invoke(view) }
//    view.setOnClickListener {
//        onClick.invoke(it)
//    }
}

inline fun View.click(@IdRes id: Int, crossinline onClick: (View) -> Unit) {
    val view: View = this.findViewById(id)
    click(view, onClick)
}