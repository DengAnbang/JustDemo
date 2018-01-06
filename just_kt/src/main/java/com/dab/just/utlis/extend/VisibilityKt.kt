package com.dab.just.utlis.extend

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View
import org.jetbrains.anko.find

/**
 * Created by dab on 2018/1/2 0002 11:18
 */
fun View.visibility(@IdRes id: Int, visible: Boolean) {
    visibility(find<View>(id),visible)
}

fun Activity.visibility(@IdRes id: Int, visible: Boolean) {
    visibility(find<View>(id),visible)
}

fun visibility(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}