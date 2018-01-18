package com.dab.just.utlis.kt

import android.app.Activity
import android.content.res.Resources
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.design.widget.TabLayout
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.find
import java.lang.reflect.Field

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


fun View?.initStatusBar() {
    if (this==null)return
    var statusBarHeight = 0
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
        //获取status_bar_height资源的ID
        val resourceId = this.context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = this.context.resources.getDimensionPixelSize(resourceId)
        }
    }
    val layoutParams = this.layoutParams
    layoutParams.height = statusBarHeight
    this.layoutParams = layoutParams
}

fun TabLayout.setIndicator(leftDip:Int,rightDip:Int) {
    val tabLayout = this.javaClass
    var tabStrip: Field? = null
    try {
        tabStrip = tabLayout.getDeclaredField("mTabStrip")
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    }
    tabStrip!!.isAccessible = true
    var llTab: LinearLayout? = null
    try {
        llTab = tabStrip.get(this) as LinearLayout
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }
    val left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip.toFloat(), Resources.getSystem().displayMetrics).toInt()
    val right = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip.toFloat(), Resources.getSystem().displayMetrics).toInt()

    for (i in 0 until llTab!!.childCount) {
        val child = llTab.getChildAt(i)
        child.setPadding(0, 0, 0, 0)
        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        params.leftMargin = left
        params.rightMargin = right
        child.layoutParams = params
        child.invalidate()
    }
}