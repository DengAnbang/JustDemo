package com.dab.just.utlis.extend

import android.Manifest
import android.app.Activity
import com.dab.just.utlis.ToastUtils.showToast
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Created by dab on 2017/11/22 0022 15:27
 */
fun Activity.rxPermissionsLocation(agree: () -> Unit) {
    val rxPermissions = RxPermissions(this)
    rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
            .subscribe {
                if (it) {
                    agree.invoke()
                } else {
                    showToast("没有定位权限")
                }
            }

}

fun Activity.rxPermissionsContacts(agree: () -> Unit) {
    val rxPermissions = RxPermissions(this)
    rxPermissions.request(Manifest.permission.READ_CONTACTS)
            .subscribe {
                if (it) {
                    agree.invoke()
                } else {
                    showToast("没有访问通讯录权限，请先授权")
                }
            }
}

fun Activity.rxPermissionsWrite(agree: () -> Unit) {
    val rxPermissions = RxPermissions(this)
    rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe {
                if (it) {
                    agree.invoke()
                } else {
                    showToast("没有访问本地存储权限，请先授权")
                }
            }
}