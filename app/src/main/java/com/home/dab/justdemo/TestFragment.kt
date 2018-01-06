package com.home.dab.justdemo

import android.view.View

import com.dab.just.base.LazyFragment

/**
 * Created by dab on 2018/1/6 0006 12:51
 */

class TestFragment : LazyFragment() {
    override fun onFirstVisibleToUser(view: View?) {

    }

    override fun viewLayoutID(): Int = R.layout.fragment_text
}
