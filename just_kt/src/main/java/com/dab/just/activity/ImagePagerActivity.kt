package com.dab.just.activity

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import com.dab.just.R
import com.dab.just.base.BaseJustActivity
import com.dab.just.custom.PinchImageView
import com.dab.just.utlis.kt.setText
import org.jetbrains.anko.find

class ImagePagerActivity : BaseJustActivity() {
    override fun setContentViewRes(): Int = R.layout.activity_image_pager

    var urls: ArrayList<String> = ArrayList()

    companion object {
        val POSITION = "position"
        val URLS = "url"
    }
    private val pageChangeListener: ViewPager.SimpleOnPageChangeListener = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            setText(R.id.tv_pages, "${position + 1}/${urls.size}")
        }
    }
    override fun initView() {
        super.initView()
        title="查看图片"
        val position = intent.getIntExtra(POSITION, 0)

        val urls = intent.getStringArrayListExtra(URLS)
        if (urls != null) {
            this.urls.addAll(urls)
        }
        find<ViewPager>(R.id.mViewPager).apply {
            addOnPageChangeListener(pageChangeListener)
            adapter = ImagePagerAdapter( urls)
            currentItem = position
        }
        setText(R.id.tv_pages, "${position + 1}/${urls.size}")
    }


    override fun onDestroy() {
        super.onDestroy()
        find<ViewPager>(R.id.mViewPager).removeOnPageChangeListener(pageChangeListener)
    }


    inner class ImagePagerAdapter(private val urls: ArrayList<String>) : PagerAdapter() {
        override fun isViewFromObject(view: View?, `object`: Any?)=view==`object`
        override fun getCount()=urls.size
        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            val pinchImageView = PinchImageView(this@ImagePagerActivity)
            container?.addView(pinchImageView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            pinchImageView.setImage(urls[position])
            return pinchImageView
        }
    }

}
