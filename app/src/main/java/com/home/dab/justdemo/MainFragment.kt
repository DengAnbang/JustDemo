package com.home.dab.justdemo

import android.app.Activity
import android.content.Intent
import android.view.View
import com.dab.just.activity.SelectPhotoDialogActivity
import com.dab.just.base.ImageViewJust
import com.dab.just.base.LazyFragment
import com.dab.just.utlis.kt.click
import com.dab.just.utlis.kt.loge
import com.dab.just.utlis.kt.requestSucceed
import com.home.dab.justdemo.net.HttpManager
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.startActivityForResult


/**
 * Created by dab on 2018/1/6 0006 12:51
 */

class MainFragment : LazyFragment() {
    override fun viewLayoutID(): Int = R.layout.fragment_main

    override fun onFirstVisibleToUser(view: View?) {
        view?.apply {
            click(R.id.btn_photo) {
                startActivityForResult<SelectPhotoDialogActivity>(55)
            }
            click(R.id.btn_login) {
                HttpManager.paswLogin("123", "123456")
                        .requestSucceed(this@MainFragment) {
                            loge(it)
                        }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 55 && data != null) {
                val path = data.getStringExtra(SelectPhotoDialogActivity.PATH)
                loge(path)

                find<ImageViewJust>(R.id.ziv_test)
                        .setImageRound()
//                        .setImage("http://img.lanrentuku.com/img/allimg/1609/14747974667766.jpg")
                        .setImage(path)
            }
        }
    }
}