package com.dab.just.utlis.extend

import android.app.Activity
import android.view.Gravity
import com.dab.just.custom.BasePopupWindow

/**
 * Created by dab on 2018/1/5 0005 17:21
 */
fun Activity.showPopupWindow(window: BasePopupWindow, gravity: Int = Gravity.CENTER, x: Int = 0, y: Int = 0) {
    if (this.isFinishing) {
        loge(" Activity isFinishing", 2, "err")
        return
    }
    if (window.isShowing) {
        loge(" window isShowing", 2, "err")
        return
    }
    val decorView = this.window.decorView
    if (decorView == null) {
        loge(" decorView is null", 2, "err")
        return
    }
    window.showAtLocation(this, decorView, gravity, x, y)
}
