package com.dab.just.utlis.kt

import android.app.Activity
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.find

/**
 * Created by dab on 2018/1/2 0002 11:18
 */

fun Activity.setText(@IdRes id: Int, string: CharSequence?, @ColorRes colorRes: Int = 0): TextView {
    val find = find<TextView>(id)
    if (colorRes != 0) {
        find.setTextColor(getColorKt(colorRes))
    }
    find.text = string ?: ""
    return find
}
fun View.setText(@IdRes id: Int, string: CharSequence?, @ColorRes colorRes: Int = 0): TextView {
    val find = find<TextView>(id)
    if (colorRes != 0) {
        find.setTextColor(context.getColorKt(colorRes))
    }
    find.text = string ?: ""
    return find
}
fun View.getTextViewString(@IdRes id: Int): String {
    val find = find<TextView>(id)
    return find.text.toString().trim()
}
fun Activity.getTextViewString(@IdRes id: Int): String {
    val find = find<TextView>(id)
    return find.text.toString().trim()
}

fun View.setBackground(@IdRes id: Int, @DrawableRes res: Int) {
    find<View>(id).backgroundResource = res
}

fun initStatusBar(view: View) {
    var statusBarHeight = 0
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
        //获取status_bar_height资源的ID
        val resourceId = view.context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = view.context.resources.getDimensionPixelSize(resourceId)
        }
    }
    val layoutParams = view.layoutParams
    layoutParams.height = statusBarHeight
    view.layoutParams = layoutParams
}