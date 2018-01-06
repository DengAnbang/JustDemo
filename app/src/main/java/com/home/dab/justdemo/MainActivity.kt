package com.home.dab.justdemo

import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dab.just.JustConfig
import com.dab.just.base.BottomTabActivity
import com.dab.just.utlis.ToastUtils.showToast


class MainActivity : BottomTabActivity() {
    override fun fragments(): Array<Fragment> = arrayOf(
            TestFragment()
            , TestFragment()
            , TestFragment()
    )

    private val mTabTitle = arrayOf("第一个", "第二个", "第三个")
    private val mTabRes = intArrayOf(R.mipmap.xingbie_nan, R.mipmap.xingbie_nan, R.mipmap.xingbie_nan)
    private val mTabResPressed = intArrayOf(R.mipmap.xingbie_nv, R.mipmap.xingbie_nv, R.mipmap.xingbie_nv)


    override fun getTabViewResID(position: Int): Int = R.layout.layout_bottom_tab_view

    override fun changeTab(view: View, position: Int, isSelected: Boolean) {
        val tabIconRes = if (isSelected) mTabResPressed[position] else mTabRes[position]
        val color = if (isSelected) R.color.colorAccent else R.color.colorPrimaryDark
        val tabIcon: ImageView = view.findViewById(R.id.tab_content_image)
        tabIcon.setImageResource(tabIconRes)
        val tabText: TextView = view.findViewById(R.id.tab_content_text)
        tabText.text = mTabTitle[position]
        tabText.setTextColor(ContextCompat.getColor(this, color))
    }

    override fun initView() {
        super.initView()
        JustConfig.init(this)
        title = "asdada"
    }

    /**
     * 默认显示第几个按钮的
     */
//    override fun showTab(): Int = 2

    private var firstTime: Long = 0

    override fun onBackPressed() {
        val secondTime = System.currentTimeMillis()
        if (secondTime - firstTime > 2000) {
            showToast("再按一次退出程序")
            firstTime = secondTime
        } else {
            super.onBackPressed()
        }
    }
}
