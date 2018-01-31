package com.dab.just.utlis.kt

import android.app.Activity
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.view.View
import android.widget.TextView
import com.dab.just.custom.TwoTextLinearView
import org.jetbrains.anko.find

/**
 * Created by dab on 2018/1/11 0011 15:53
 */
fun Activity.setTwoTextViewRight(@IdRes id: Int, string: CharSequence?, @ColorRes colorRes: Int = 0) :TextView{
   return find<TwoTextLinearView>(id). apply {
        rightText=string
        if (colorRes != 0) {
            setRightTextColor(context.getColorKt(colorRes))
        }
    }.rightView
}
fun View.setTwoTextViewRight(@IdRes id: Int, string: CharSequence?, @ColorRes colorRes: Int = 0) {
    find<TwoTextLinearView>(id). apply {
        rightText=string
        if (colorRes != 0) {
            setRightTextColor(context.getColorKt(colorRes))
        }
    }
}
fun View.getTwoTextViewRight(@IdRes id: Int) =find<TwoTextLinearView>(id).rightText.toString()
fun Activity.getTwoTextViewRight(@IdRes id: Int) =find<TwoTextLinearView>(id).rightText.toString()