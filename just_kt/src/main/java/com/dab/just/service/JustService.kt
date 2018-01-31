package com.dab.just.service

import android.app.IntentService
import android.content.Intent
import android.net.Uri
import com.dab.just.JustConfig
import com.dab.just.activity.UpgradeVersionActivity
import com.dab.just.net.http.JustHttpManager
import com.dab.just.net.http.UpdateUtils
import com.dab.just.utlis.kt.loge
import java.io.File

/**
 * Created by Wendell on 2018/1/30 16:53
 */
class JustService : IntentService("just") {
    override fun onHandleIntent(intent: Intent?) {
        if (intent==null) return
        val type = intent.getStringExtra("type")
        when (type) {
            UpgradeVersionActivity.UPGRADE_VERSION -> upgrade(intent.getStringExtra(UpgradeVersionActivity.path))
        }
    }

    private fun upgrade(path: String?) {
        if (path.isNullOrEmpty())return
            loge(path)

        UpdateUtils.download(JustHttpManager.BASE_URL+path, JustConfig.getTestBaseFilePath()){
            progress, total, saveDir, e ->
            loge(progress)
            loge(total)
            loge(saveDir)
            e?.printStackTrace()
            if (progress>0&&progress==total) {
                val intent= Intent(Intent.ACTION_VIEW)
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                intent.setDataAndType(Uri.fromFile(File(saveDir)), "application/vnd.android.package-archive")
                startActivity(intent)
            }
            false
        }

//        val intent= Intent(Intent.ACTION_VIEW)
//        // 由于没有在Activity环境下启动Activity,设置下面的标签
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
//        intent.setDataAndType(Uri.fromFile(File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/cache/5f1d92a5-38f4-48f9-bf69-6d5e76f7d209.apk")), "application/vnd.android.package-archive")
//        startActivity(intent)

    }

}