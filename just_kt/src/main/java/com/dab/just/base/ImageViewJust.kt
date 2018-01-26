package com.dab.just.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.dab.just.R
import com.dab.just.interfaces.ImageViewPromise


/**
 * Created by dab on 2018/1/8 0008 11:04
 * 图片加载控件,将常用功能抽象成ImageViewPromise接口了,默认使用Glide实现,如果要换加载框架,就修改这个类就好了
 */
open class ImageViewJust : android.support.v7.widget.AppCompatImageView, ImageViewPromise {
    companion object {
        fun setImage(imageView: ImageView,url: String) {
            Glide.with(imageView.context)
                    .load(url)
                    .into(imageView)
        }
    }
    open var requestOptions = RequestOptions()
    private var defaultSrc = -1
    private var defaultError = R.mipmap.c1_img1
    private var defaultPlaceholder = R.mipmap.c1_img1
    private var defaultAngleLeft = -1
    private var defaultAngleTop = -1
    private var defaultAngleRight = -1
    private var defaultAngleBottom = -1
    private var defaultRounding = false

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ImageViewJust)
        defaultSrc = obtainStyledAttributes.getResourceId(R.styleable.ImageViewJust_src, -1)
        defaultError = obtainStyledAttributes.getResourceId(R.styleable.ImageViewJust_error, defaultError)
        defaultPlaceholder = obtainStyledAttributes.getResourceId(R.styleable.ImageViewJust_placeholder, defaultPlaceholder)
        defaultRounding = obtainStyledAttributes.getBoolean(R.styleable.ImageViewJust_defaultRounding, defaultRounding)
        defaultAngleLeft = obtainStyledAttributes.getInteger(R.styleable.ImageViewJust_angle_left, -1)
        defaultAngleTop = obtainStyledAttributes.getInteger(R.styleable.ImageViewJust_angle_top, -1)
        defaultAngleRight = obtainStyledAttributes.getInteger(R.styleable.ImageViewJust_angle_right, -1)
        defaultAngleBottom = obtainStyledAttributes.getInteger(R.styleable.ImageViewJust_angle_bottom, -1)
        obtainStyledAttributes.recycle()
        init()
    }

    @SuppressLint("CheckResult")
    private fun init() {
        if (defaultSrc != -1) {
            setImage(defaultSrc)
        }
        if (defaultError != -1) {
            requestOptions.error(defaultError)
        }
        if (defaultPlaceholder != -1) {
            requestOptions.placeholder(defaultPlaceholder)
        }
        if (defaultPlaceholder != -1) {
            requestOptions.placeholder(defaultPlaceholder)
        }
        if (defaultRounding) {
            requestOptions.transform(CircleCrop())
        }

    }

    override fun setImage(url: String): ImageViewPromise {
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(url)
                .into(this)
        return this
    }
    override fun setImage(bitmap: Bitmap): ImageViewPromise {
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(bitmap)
                .into(this)
        return this
    }


    override fun setImage(res: Int): ImageViewPromise {
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(res)
                .into(this)
        return this
    }

    override fun setImageAngle(left: Int, top: Int, right: Int, bottom: Int): ImageViewPromise {
        throw UnsupportedOperationException("未实现这个方法")
        return this
    }

    @SuppressLint("CheckResult")
    override fun setImageRound(): ImageViewPromise {
        requestOptions.transform(CircleCrop())
        return this
    }

    override fun setImageByFile(path: String): ImageViewPromise {
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(path)
                .into(this)
        return this
    }

    @SuppressLint("CheckResult")
    override fun setPlaceholderImage(res: Int): ImageViewPromise {
        if (defaultPlaceholder != -1) {
            requestOptions.placeholder(res)
        }
        return this
    }

    @SuppressLint("CheckResult")
    override fun setFailureImage(res: Int): ImageViewPromise {
        if (defaultError != -1) {
            requestOptions.error(res)
        }
        return this
    }
}