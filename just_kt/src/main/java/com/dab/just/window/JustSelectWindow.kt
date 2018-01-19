package com.dab.just.window

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil.dp2px
import com.dab.just.R
import com.dab.just.base.BasePopupWindow
import com.dab.just.utlis.kt.click
import com.dab.just.utlis.kt.getColorKt
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find

/**
 * Created by dab on 2018/1/19 0019 14:59
 */
class JustSelectWindow(ctx: Activity) : BasePopupWindow(ctx) {
    override fun setContentRes()= R.layout.window_just_select
    fun addText(msg: String, onClick: (view: View) -> Unit): TextView{
        val textView = TextView(contentView.context).apply {
            click(this) {
                dismiss()
                onClick.invoke(this)
            }
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 50f)).apply {
                setMargins(dp2px(context, 0f),dp2px(context, 1f),dp2px(context, 0f),0)
            }
            gravity=Gravity.CENTER
            text = msg
            backgroundColor=context.getColorKt(R.color.just_color_ffffff)
        }
        contentView.find<LinearLayout>(R.id.layout_just_select).addView(textView)
        return textView
    }
}