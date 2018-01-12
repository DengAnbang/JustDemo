package com.dab.just.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dab.just.R
import com.dab.just.base.BaseJustActivity
import org.jetbrains.anko.find


class WebViewActivity : BaseJustActivity() {
    override fun setContentViewRes(): Int=R.layout.activity_web_view

    private val webView by lazy {
        find<WebView>(R.id.webView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        super.initView()
        val t = intent.getStringExtra("title")
        title = t
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.defaultTextEncodingName = "utf-8"
        settings.domStorageEnabled = true
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String?) {
                super.onReceivedTitle(view, title)
                if (resetTitle()) {
                    setTitle(title)
                }
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String?) {
                super.onPageFinished(view, url)
                dismissLoadDialog()
            }

            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showLoadDialog()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                if (!TextUtils.isEmpty(url)) {
                    webView.loadUrl(url)
                }
                return true
            }
        }
        val type = intent.getIntExtra("type", 0)
        val url = intent.getStringExtra("url")
        if (type == 1) {
            webView.loadData(url, "text/html; charset=utf-8", "utf-8")
        } else {
            webView.loadUrl(url)
        }
    }

     open fun resetTitle() = false

}
