package com.dab.just.activity

import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RelativeLayout
import com.dab.just.R
import com.dab.just.base.BaseJustActivity
import com.dab.just.utlis.kt.click
import com.dab.just.utlis.kt.visibility
import org.jetbrains.anko.find

/**
 * Created by dab on 2018/1/10 0010 10:06
 */
abstract class JustGuiderActivity : BaseJustActivity(){
    override fun fullScreen(): Boolean =true
    override fun setContentViewRes(): Int = R.layout.activity_just_guide
    abstract fun getImages():Array<Int>
    abstract fun next()
    override fun initView() {
        super.initView()
        find<ViewPager>(R.id.viewpager).apply {
            adapter= GuideAdapter(getImages())
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> this@JustGuiderActivity.find<RadioButton>(R.id.radiobutton1).isChecked = true
                        1 -> this@JustGuiderActivity.find<RadioButton>(R.id.radiobutton2).isChecked = true
                        2 -> this@JustGuiderActivity.find<RadioButton>(R.id.radiobutton3).isChecked = true
                        3 -> this@JustGuiderActivity.find<RadioButton>(R.id.radiobutton4).isChecked = true
                    }
                    this@JustGuiderActivity.visibility(R.id.btn_skip,position!=3)
                    this@JustGuiderActivity.visibility(R.id.tv_finish,position==3)
                }
            })
        }
        click(R.id.btn_skip) { next()}
        click(R.id.tv_finish) { next()}
    }


    internal inner class GuideAdapter(val images:Array<Int>) : PagerAdapter() {

        override fun getCount(): Int {
            return images.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val relativeLayout = RelativeLayout(
                    container.context)
            val res = images[position]
            val photoView = ImageView(container.context)
            photoView.setBackgroundColor(Color.WHITE)
            photoView.scaleType = ImageView.ScaleType.FIT_XY
            photoView.setImageResource(res)
            relativeLayout.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            container.addView(relativeLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            return relativeLayout
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }
}