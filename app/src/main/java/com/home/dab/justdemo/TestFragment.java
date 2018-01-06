package com.home.dab.justdemo;

import android.view.View;

import com.dab.just.base.LazyFragment;

import org.jetbrains.annotations.Nullable;

/**
 * Created by dab on 2018/1/6 0006 12:51
 */

public class TestFragment extends LazyFragment{
    @Override
    protected void onFirstVisibleToUser(@Nullable View view) {

    }

    @Override
    protected int viewLayoutID() {
        return R.layout.fragment_text;
    }
}
