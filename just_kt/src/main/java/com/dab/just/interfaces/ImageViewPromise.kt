package com.dab.just.interfaces

import android.support.annotation.DrawableRes

/**
 * Created by dab on 2017/10/23 0023 09:28
 * 图片控件的接口
 */

interface ImageViewPromise {
    /**
     * 设置加载网络图片
     *
     * @param url
     */
    fun setImage(url: String):ImageViewPromise

    /**
     * 设置加载本地app内图片
     *
     * @param res
     */
    fun setImage(@DrawableRes res: Int):ImageViewPromise

    /**
     * 设置图片角度
     */
    fun setImageAngle(left:Int,top:Int,right:Int,bottom:Int):ImageViewPromise

    /**
     * 设置图片为圆形
     */
    fun setImageRound(roundingRadius:Int):ImageViewPromise

    /**
     * 设置加载本地图片
     *
     * @param path
     */
    fun setImageByFile(path: String):ImageViewPromise

    /**
     * 设置占位图
     *
     * @param res
     */
    fun setPlaceholderImage(res: Int):ImageViewPromise

    /**
     * 设置失败的图片
     *
     * @param res
     */
    fun setFailureImage(res: Int):ImageViewPromise
}
