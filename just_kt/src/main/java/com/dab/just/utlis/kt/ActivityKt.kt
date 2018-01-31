package com.dab.just.utlis.kt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.view.Gravity
import com.dab.just.activity.UpgradeVersionActivity
import com.dab.just.base.BasePopupWindow
import org.jetbrains.anko.startActivityForResult

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
    window.showAtLocation(decorView, gravity, x, y)
}
fun Fragment.showPopupWindow(window: BasePopupWindow, gravity: Int = Gravity.CENTER, x: Int = 0, y: Int = 0) {
    if (!this.isVisible) {
        loge(" Activity isFinishing", 2, "err")
        return
    }
    if (window.isShowing) {
        loge(" window isShowing", 2, "err")
        return
    }
    val decorView = this.view?.rootView
    if (decorView == null) {
        loge(" decorView is null", 2, "err")
        return
    }
    window.showAtLocation( decorView, gravity, x, y)
}

/**
 * 打电话
 */
fun Activity.callPhone(phone:String?){
    val p = phone ?: ""
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:" + p)
    this.startActivity(intent)
}

/**
 * 获取版本名字
 */
fun Activity.getVersionName():String {
    return try {
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, 0)
        info.versionName
    } catch (e: Exception) {
        e.printStackTrace()
        "--"
    }
}
/**
 * 获取版本号
 */
fun Activity.getVersionCode():Int {
    return try {
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, 0)
        info.versionCode
    } catch (e: Exception) {
        e.printStackTrace()
        1
    }
}

/**
 * 版本升级
 * requestCode: RESULT_CANCELED 表示点击了取消,并且取消的是强制升级,就退出应用
 *              RESULT_OK 表示继续操作,可能点了升级,也可能点了取消,但是不是强制升级的
 * constraint:是否强制升级
 * time:更新时间
 */
fun Activity.upgradeVersion(requestCode:Int,versionCode: Int, versionName: String, md5: String, content: String, constraint: Boolean, path: String, time: String = "", block: () -> Unit) {
    if (versionCode > getVersionCode()) {
        startActivityForResult<UpgradeVersionActivity>(requestCode,
                UpgradeVersionActivity.versionName to versionName
                , UpgradeVersionActivity.time to time
                , UpgradeVersionActivity.path to path
                , UpgradeVersionActivity.md5 to md5
                , UpgradeVersionActivity.content to content
                , UpgradeVersionActivity.CONSTRAINT to constraint
        )
    } else {
        block.invoke()
    }

}