package com.dab.just.base


import android.content.Context
import android.support.annotation.LayoutRes
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.dab.just.R


/**
 * Created by dab on 2017/8/7 0007 15:21
 * 底部按钮的父类
 */

abstract class BottomTabActivity : BaseJustActivity() {
    lateinit var mTabLayout: TabLayout

    lateinit open var mFragmensts: Array<Fragment>
    private val TAG = "BottomTabActivity"
    /**
     * 获取Fragment的集合

     * @return
     */
    abstract fun fragments(): Array<Fragment>

    /**
     * 获取底部按钮的样式

     * @return
     */
    @LayoutRes
    abstract fun getTabViewResID(position: Int): Int

    /**
     * 修改底部图标的样式

     * @param position
     * *
     * @param isSelected 是否选中
     */
    abstract fun changeTab(view: View, position: Int, isSelected: Boolean)

    /**
     * 显示第几个tab(默认显示第一个)

     * @return
     */
    open fun showTab(): Int = 0

    /**
     * 获取Tab 显示的内容

     * @param context
     * *
     * @param position
     * *
     * @return
     */
    open fun getTabView(context: Context, position: Int): View = LayoutInflater.from(context).inflate(getTabViewResID(position), null)

    final override fun setContentViewRes(): Int = R.layout.layout_bottom_tab

    override fun initView() {
        super.initView()
        mFragmensts = fragments()
        mTabLayout = findViewById(R.id.bottom_tab_layout)
        addFragment()
        mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                onTabItemSelected(tab.position)
                // Tab 选中之后，改变各个Tab的状态
                for (i in 0 until mTabLayout.tabCount) {
                    val tabAt = mTabLayout.getTabAt(i)
                    if (tabAt == null) {
                        Log.e(TAG, "tabAt==null")
                        continue
                    }
                    val view = tabAt.customView
                    if (view == null) {
                        Log.e(TAG, "onTabSelected*****: view==null")
                        continue
                    }
                    changeTab(view, i, i == tab.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        var showTab = showTab()
        if (showTab >= mFragmensts.size) {
            showTab = 0
        }
        // 提供自定义的布局添加Tab
        for (i in mFragmensts.indices) {
            val tabView = getTabView(this, i)
            val tab = mTabLayout.newTab().setCustomView(tabView)
            mTabLayout.addTab(tab, i == showTab)
            changeTab(tabView, i, i == showTab)
        }
    }

    private fun addFragment() {
        mFragmensts.indices
                .map { mFragmensts[it] }
                .forEach { supportFragmentManager.beginTransaction().add(R.id.home_container, it).commit() }
    }

    private fun onTabItemSelected(position: Int) {
        if (mFragmensts.size < position) return
        for (i in mFragmensts.indices) {
            val fragment = mFragmensts[i]
            if (i == position) {
                supportFragmentManager.beginTransaction().show(fragment).commit()
            } else {
                supportFragmentManager.beginTransaction().hide(fragment).commit()
            }

        }
    }
}
