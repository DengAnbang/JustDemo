package com.dab.just.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.Gravity
import com.dab.just.JustConfig
import com.dab.just.R
import com.dab.just.base.BaseDialogActivity
import com.dab.just.utlis.kt.click
import com.dab.just.utlis.kt.rxPermissionsRead
import com.dab.just.utlis.kt.rxPermissionsWrite
import java.io.File
import java.io.IOException
import java.util.*



class SelectVideoDialogActivity : BaseDialogActivity() {
    override fun setContentViewRes(): Int = R.layout.activity_select_video_dialog
    private var tempFile: File? = null
    private val maxDuration by lazy {
        intent.getIntExtra(MAX_DURATION_SECOND, 0)
    }

    companion object {
        val PATH = "path"
        val SIZE = "size"//拍摄的时候目前不会拿到
        val DURATION = "duration"//拍摄的时候目前不会拿到
        val MAX_DURATION_SECOND = "max_duration_second"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setGravity(Gravity.BOTTOM)
        //取消
        click(android.R.id.button3) { onBackPressed() }
        click(android.R.id.button1) {
            rxPermissionsWrite {
                val timeStamp = System.currentTimeMillis().toString()
                val imageFileName = "VID_" + timeStamp
                val suffix = ".mp4"
                tempFile = File(JustConfig.getBaseFilePath(), imageFileName + suffix)
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                try {
                    val fileUri = Uri.fromFile(tempFile)
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
                    if (maxDuration != 0) {
                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, maxDuration)//限制录制时间10秒
                    }
                    //7.0崩溃问题
                    if (Build.VERSION.SDK_INT < 24) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                    } else {
                        val contentValues = ContentValues(1)
                        contentValues.put(MediaStore.Video.Media.DATA, tempFile?.absolutePath)
                        val uri = this@SelectVideoDialogActivity.contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    }

                    startActivityForResult(intent, 0x66)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        //选择文件
        click(android.R.id.button2) {
            rxPermissionsRead {
                //这个是选取文件,不能获取到时长
//            val intent= Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//            intent.type = "video/*"
//            startActivityForResult(intent, 1)
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "video/*"
                startActivityForResult(intent, 0x67)
            }
        }
    }

    override fun exitAnim(): Int {
        return R.anim.popup_out
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                0x66 -> {
                    if (data == null) {
                        showToast("录制失败")
                        return
                    }
                    if (duration  > maxDuration*1000) {
                        showToast("最多只能选取时长为${maxDuration}秒的视频!")
                        return
                    }
                    val uri = data.data
                    val path = getPath(uri)
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(path)
                    mediaPlayer.prepare()
                    duration = mediaPlayer.duration.toLong()
                    val intent = Intent()
                    intent.putExtra(PATH, path)
                    intent.putExtra(SIZE, size)
                    intent.putExtra(DURATION, duration)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                0x67 -> {
                    //选择文件
                    if (data == null) {
                        showToast("无法识别的视频类型！")
                        return
                    }
                    val uri = data.data
                    val path = getPath(uri)
                    if (path == null) {
                        showToast("无法识别的视频的路径！")
                        return
                    }
                    val typeIndex = path.lastIndexOf(".")
                    if (typeIndex == -1) {
                        showToast("无法识别的视频类型！")
                        return
                    }
                    val fileType = path.substring(typeIndex + 1).toLowerCase(Locale.CHINA)
                    if (fileType == "mp4" || fileType == "3gp") {
                        if (duration  > maxDuration*1000) {
                            showToast("最多只能选取时长为${maxDuration}秒的视频!")
                            return
                        }
                        val intent = Intent()
                        intent.putExtra(PATH, path)
                        intent.putExtra(SIZE, size)
                        intent.putExtra(DURATION, duration)
                        setResult(RESULT_OK, intent)
                        finish()
//                        overridePendingTransition(0, 0)
                        //			                        	cropImage(path);
                        //			                        	BitmapUtil.getInstance(this).loadImage(iv_image, path);
                    } else {
                        showToast("无法识别的视频类型！")
                    }

                }
            }
        }
    }

    private fun getPath(uri: Uri): String? {
        var imagePath: String? = null
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri,则通过document id来处理
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                val id = docId.split(":")[1]
                val selection = MediaStore.Video.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, selection)
            }
        } else if ("content".equals(uri.scheme, true)) {
            //如果是content类型的Uri,则使用普通的方式处理
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, true)) {
            //如果是file类型的Uri，则直接获取视频路径即可
            imagePath = uri.path
        }
        return imagePath
    }

    var size: Long = 0
    var duration: Long = 0
    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        size = 0
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
            size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
            duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
        }
        cursor.close()
        return path
    }
}