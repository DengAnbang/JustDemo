package com.dab.just.utlis.extend

import android.app.Activity
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

fun View.setBackground(@IdRes id: Int, @DrawableRes res: Int) {
    find<View>(id).backgroundResource = res
}