package com.dab.just.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zbar.ZBarView
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.dab.just.R
import com.dab.just.base.BaseJustActivity
import com.dab.just.utlis.kt.rxPermissionsCamera
import org.jetbrains.anko.find


/**
 * Created by dab on 2018/1/15 0015 16:05
 * 扫描二维码
 */
class ScanQrActivity : BaseJustActivity() {
    companion object {
        val QR_CONTENT = "QR_CONTENT"
        fun syncEncodeQRCode(content:String, sizePx:Int, foregroundColor:Int= Color.BLACK, backgroundColor:Int=Color.WHITE, logo:Bitmap?=null): Bitmap =QRCodeEncoder.syncEncodeQRCode(content,sizePx,foregroundColor,backgroundColor,logo)
    }
    override fun setContentViewRes()= R.layout.activity_scan
    private val mQRCodeView by lazy {
        find<ZBarView>(R.id.zbarview)
    }
    override fun initView() {
        super.initView()
        title="扫一扫"
        mQRCodeView.setDelegate(object : QRCodeView.Delegate {
            override fun onScanQRCodeSuccess(result: String?) {
//                showToast(""+result)
//                mQRCodeView.startSpot()
                setResult(Activity.RESULT_OK, Intent().putExtra(QR_CONTENT,result))
                finish()
            }

            override fun onScanQRCodeOpenCameraError() {
                showToast("打开相机失败,请检查相机权限是否打开")
            }

        })
    }

    override fun onStart() {
        super.onStart()
        rxPermissionsCamera{
            mQRCodeView.startCamera()
            //        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mQRCodeView.showScanRect()
            mQRCodeView.startSpot()
        }

    }

    override fun onStop() {
        mQRCodeView.stopCamera()
        super.onStop()
    }

    override fun onDestroy() {
        mQRCodeView.onDestroy()
        super.onDestroy()
    }
}