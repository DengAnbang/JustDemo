package com.dab.just.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.Gravity
import com.dab.just.JustConfig
import com.dab.just.R
import com.dab.just.base.BaseDialogActivity
import com.dab.just.utlis.extend.click
import com.dab.just.utlis.extend.isTrue
import com.dab.just.utlis.extend.rxPermissionsWrite
import org.jetbrains.anko.toast
import java.io.File
import java.util.*

class SelectPhotoDialogActivity : BaseDialogActivity() {
    override fun setContentViewRes(): Int = R.layout.activity_select_photo_dialog

    private var tempFile: File? = null

    companion object {
        val PATH = "path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setGravity(Gravity.BOTTOM)
        //取消
        click(android.R.id.button3) { onBackPressed() }
        click(android.R.id.button1) {
            rxPermissionsWrite {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val fileName = System.currentTimeMillis().toString() + ".jpg"
                tempFile = File(JustConfig.getBaseFilePath(), fileName)
                val u = Uri.fromFile(tempFile)
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
                //7.0崩溃问题
                if (Build.VERSION.SDK_INT < 24) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, u)
                } else {
                    val contentValues = ContentValues(1)
                    contentValues.put(MediaStore.Images.Media.DATA, tempFile?.absolutePath)
                    val uri = this@SelectPhotoDialogActivity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                }
                startActivityForResult(intent, 0)
            }
        }
        //选择相册
        click(android.R.id.button2) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)// 调用android的图库
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }

    override fun exitAnim(): Int {
        return R.anim.popup_out
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                0 -> {
                    //拍照
                    if (tempFile?.exists().isTrue()) {
                        val intent = Intent()
                        intent.putExtra(PATH, tempFile?.absolutePath)
                        setResult(RESULT_OK, intent)
                        onBackPressed()
                        overridePendingTransition(0, 0)
                    }
                }
                1 -> {
                    //相册
                    if (data == null) {
                        showToast("无法识别的图片类型！")
                        return
                    }
                    val uri = data.data
                    val path = getPath(uri)
                    if (path == null) {
                        showToast("无法识别的图片的路径！")
                        return
                    }
                    val typeIndex = path.lastIndexOf(".")
                    if (typeIndex == -1) {
                        showToast("无法识别的图片类型！")
                        return
                    }
                    val fileType = path.substring(typeIndex + 1).toLowerCase(Locale.CHINA)
                    if (fileType == "jpg" || fileType == "gif"
                            || fileType == "png" || fileType == "jpeg"
                            || fileType == "bmp" || fileType == "wbmp"
                            || fileType == "ico" || fileType == "jpe") {
                        val intent = Intent()
                        intent.putExtra(PATH, path)
                        setResult(RESULT_OK, intent)
                        finish()
                        overridePendingTransition(0, 0)
                        //			                        	cropImage(path);
                        //			                        	BitmapUtil.getInstance(this).loadImage(iv_image, path);
                    } else {
                        toast("无法识别的图片类型！")
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
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            }
        } else if ("content".equals(uri.scheme, true)) {
            //如果是content类型的Uri,则使用普通的方式处理
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, true)) {
            //如果是file类型的Uri，则直接获取图片路径即可
            imagePath = uri.path
        }
        return imagePath
    }

    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        }
        cursor.close()
        return path
    }
}