package com.dab.just.utlis.extend

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.dab.just.JustConfig
import com.dab.just.bean.ResultData
import com.dab.just.bean.ResultException
import com.dab.just.interfaces.RequestHelper
import com.google.gson.JsonSyntaxException
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


/**
 * Created by dab on 2017/11/2 0002 14:06
 * 一些公共方法
 */
/**
 * 打印日志,快速定位到日志的位置,会收集栈的信息
 */
fun loge(msg: Any? = null, stack: Int = 2, myTag: String = "tanguy_loge") {
    if (!JustConfig.DeBug) return
    val stackTrace = Throwable().stackTrace
    val className = stackTrace[stack].fileName
    val methodName = stackTrace[stack].methodName
    val lineNumber = stackTrace[stack].lineNumber
    val tag = "($className:$lineNumber)"
    val msg = ("$myTag $methodName:${msg.toString()}")
    Log.e(tag, msg)
}

/**
 * 倒计时s
 */
inline fun countDownTimer(millisUntilFinish: Long, crossinline block: (finish: Boolean, millisUntilFinished: Long) -> Unit): Disposable {
    var subscribe: Disposable? = null
    subscribe = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it < millisUntilFinish) {
                    block.invoke(false, millisUntilFinish - it)
                } else {
                    block.invoke(true, 0)
                    if (subscribe != null && !subscribe!!.isDisposed) {
                        subscribe?.dispose()
                    }
                }
            }
    return subscribe
}

/**
 * 验证码的倒计时
 */
inline fun captchaCountDownTimer(crossinline block: (finish: Boolean, msg: String) -> Unit): Disposable {
    return countDownTimer(60) { finish, millisUntilFinished ->
        if (finish) {
            block.invoke(finish, "重新获取验证码")
        } else {
            block.invoke(finish, "${millisUntilFinished}s")

        }

    }
}

/**
 * 计时器(注意不需要的时候要取消订阅)
 */
inline fun rxTimer(interval: Long, crossinline block: (times: Long) -> Unit): Disposable {
    var subscribe: Disposable? = null
    subscribe = Observable.interval(0, interval, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                block.invoke(it)
            }
    return subscribe
}

/**
 * 是否包含表情
 */
fun containsEmoji(str: String): Boolean {//真为不含有表情
    val len = str.length
    for (i in 0 until len) {
        if (isEmojiCharacter(str[i])) {
            return true
        }
    }
    return false
}

private fun isEmojiCharacter(codePoint: Char): Boolean {
    return !(codePoint.toInt() == 0x0 ||
            codePoint.toInt() == 0x9 ||
            codePoint.toInt() == 0xA ||
            codePoint.toInt() == 0xD ||
            codePoint.toInt() in 0x20..0xD7FF ||
            codePoint.toInt() in 0xE000..0xFFFD ||
            codePoint.toInt() in 0x10000..0x10FFFF)
}

/**
 * 获取设备id
 */
@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

/**
 * 修改屏幕的透明度
 */
fun changeWindowAlpha(activity: Activity?, alpha: Float) {
    if (activity == null) {
        loge("changeWindowAlpha: window == null")
        return
    }

    val lp = activity.window.attributes
    lp.alpha = alpha
    activity.window.attributes = lp

}

/**
 * 隐藏车牌号（需要用到java的String类）
 */
fun hindCarNumber(carNumber: String): String {
    val string = carNumber as java.lang.String
    return string.replaceAll("(\\d{0})\\d{3}(\\d{1})", "$1***$2")
}

/**
 * 简化请求的一个方法(只有code==0时才算成功),直接在主线程返回,如果请求失败,会在这个里面提醒用户
 * 请求需要一个RequestHelper接口,因为要
 *     显示请求动画,
 *     取消动画,
 *     请求完成自动取消订阅,
 *     取消未完成时取消请求,
 *     错误提醒的弹窗提示
 *
 * 在调试的时候没有使用inline,因为内联会把回调内联进函数,日志不好记录堆栈的信息
 *
 */
fun <O, I : ResultData<O>> Observable<I>.requestSucceed(requestHelper: RequestHelper, showLoadDialog: Boolean = true, data: (data: O) -> Unit) {
    this.request(requestHelper, showLoadDialog) {
        if (it == null) return@request
        if (it.code == 0) {
            data.invoke(it.data as O)
        } else {
            requestHelper.showToast(it.msg)
        }
    }
}

/**
 * 返回所有的请求成功的数据,code!=0时也会返回
 * 如果需要code不为0时,不直接提示,而是需要处理的时候,使用这个
 */
fun <T> Observable<T>.request(requestHelper: RequestHelper, showLoadDialog: Boolean = true, data: (t: T?) -> Unit) {
    this.subscribeOn(Schedulers.io())
            .doOnSubscribe {
                if (showLoadDialog)
                    requestHelper.showLoadDialog()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<T> {
                var disposable: Disposable? = null
                override fun onError(t: Throwable) {
                    requestHelper.dismissLoadDialog()
                    var msg = t.message
                    when (t) {
                        is JsonSyntaxException -> msg = "数据解析出错！"
                        is ConnectException -> msg = "网络异常，请检查您的网络状态！"
                        is SocketTimeoutException -> msg = "网络异常，请检查您的网络状态！"
                        is HttpException -> msg = "服务器异常，请稍后重试！"
                        is ResultException -> {
                            msg = t.message
                        }
                    }
                    data.invoke(null)
                    requestHelper.showToast(msg)
                    t.printStackTrace()
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                    requestHelper.cancelRequest(d)
                }

                override fun onComplete() {
                    requestHelper.dismissLoadDialog()
                    if (disposable != null && !disposable!!.isDisposed) {
                        disposable?.dispose()
                    }
                }

                override fun onNext(t: T) {
                    requestHelper.dismissLoadDialog()
                    data.invoke(t)
                }

            })
}