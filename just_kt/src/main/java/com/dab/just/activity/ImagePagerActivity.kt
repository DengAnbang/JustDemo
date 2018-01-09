package com.dab.just.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import com.dab.just.R
import com.dab.just.base.BaseJustActivity
import com.dab.just.base.LazyFragment
import com.dab.just.custom.PinchImageView
import com.dab.just.utlis.extend.setText
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.find

class ImagePagerActivity : BaseJustActivity(), ViewPager.OnPageChangeListener {
    override fun setContentViewRes(): Int = R.layout.activity_image_pager

    var urls: ArrayList<String> = ArrayList()

    companion object {
        val POSITION = "position"
        val URLS = "url"
    }

    override fun initView() {
        super.initView()

        imagePager.addOnPageChangeListener(this)

        val position = intent.getIntExtra(POSITION, 0)
        val urls = intent.getStringArrayListExtra(URLS)
        if (urls != null) {
            this.urls.addAll(urls)
        }
        setText(R.id.tv_pages, "${position + 1}/${urls.size}")
        imagePager.adapter = ImagePagerAdapter(supportFragmentManager, urls)
        imagePager.currentItem = position
    }

    val imagePager by lazy {
        find<ViewPager>(R.id.mViewPager)
    }


    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        setText(R.id.tv_pages, "${position + 1}/${urls.size}")
    }

    override fun onDestroy() {
        super.onDestroy()
        imagePager.removeOnPageChangeListener(this)
    }

    class ImagePagerAdapter(fragmentManager: FragmentManager, urls: ArrayList<String>) : FragmentPagerAdapter(fragmentManager) {
        var mUrls = urls

        override fun getItem(position: Int): Fragment = ImageFragment.newInstance(mUrls[position])

        override fun getCount(): Int = mUrls.size
    }

    class ImageFragment : LazyFragment() {
        override fun onFirstVisibleToUser(view: View?) {
            var url: String
            if (arguments == null) {
                url = ""
            } else {
                url = arguments.getString("url")
            }
            find<PinchImageView>(R.id.piv).setImage(url)
//            val zoomDraweeView = find<ZoomableDraweeView>(R.id.zoomDrawee)
//            val hierarchy = GenericDraweeHierarchyBuilder(resources)
//                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
//                    .setProgressBarImage(ProgressBarDrawable())
//                    .build()
//            println(url)
//            val uri = Uri.parse(url)
//
//            val controller = Fresco.newDraweeControllerBuilder()
//                    .setUri(uri)
//                    .setTapToRetryEnabled(true)
//                    .build()
//
//            zoomDraweeView.hierarchy= hierarchy
//            zoomDraweeView.controller = controller
//
//            zoomDraweeView.setOnClickListener{
//                if (activity != null) {
//                    activity.onBackPressed()
//                }
//            }
        }

        override fun viewLayoutID(): Int = R.layout.fragment_image

        companion object {
            fun newInstance(url: String): Fragment {
                val fragment = ImageFragment()
                val bundle = Bundle()
                bundle.putString("url", url)
                fragment.arguments = bundle
                return fragment
            }
        }
    }
}
