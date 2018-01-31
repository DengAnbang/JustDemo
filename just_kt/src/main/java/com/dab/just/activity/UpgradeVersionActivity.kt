package com.dab.just.activity

import android.app.Activity
import com.dab.just.R
import com.dab.just.base.BaseDialogActivity
import com.dab.just.service.JustService
import com.dab.just.utlis.kt.click
import com.dab.just.utlis.kt.setText
import com.dab.just.utlis.kt.visibility
import org.jetbrains.anko.startService

/**
 * Created by Wendell on 2018/1/30 15:25
 */

class UpgradeVersionActivity : BaseDialogActivity() {
    override fun setContentViewRes(): Int=R.layout.activity_dialog_upgrade_version
    companion object {
        val UPGRADE_VERSION="upgrade_version"
        val versionName="versionName"
      val time="time"
      val content="content"
      val path="path"
      val md5="md5"
      val CONSTRAINT="constraint"
    }
    val constraint by lazy {
        intent.getBooleanExtra(CONSTRAINT,false)
    }
    override fun beforeSetContentView() {
        super.beforeSetContentView()
        setFinishOnTouchOutside(false)
    }

    override fun initView() {
        super.initView()
        setText(R.id.tv_version_name,"版本号: "+intent.getStringExtra(versionName))
        setText(R.id.tv_version_content, "版本内容:\n" + intent.getStringExtra(content))
        val time = intent.getStringExtra(time)
        if (time.isNullOrEmpty()) {
            visibility(R.id.tv_version_time,false)
        } else {
            setText(R.id.tv_version_time, "更新时间: " +time)
        }

    }

    override fun initEvent() {
        super.initEvent()
        click(R.id.tv_cancel) {
            if (constraint) {
                setResult(Activity.RESULT_CANCELED)
            } else {
                setResult(Activity.RESULT_OK)
            }
            finish()
        }
        click(R.id.tv_confirm) {
            startService<JustService>("type" to UPGRADE_VERSION,path to intent.getStringExtra(path))
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
