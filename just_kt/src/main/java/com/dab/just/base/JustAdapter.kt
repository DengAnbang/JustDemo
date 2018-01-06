package com.dab.just.base

import android.support.annotation.LayoutRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dab.just.R
import com.dab.just.net.http.HttpManager
import com.dab.just.utlis.extend.click

import java.util.*

/**
 * Created by dab on 2017/10/9 0009 13:48
 * body: (v,p) -> T
 */

abstract class JustAdapter<T>(protected val mDatas: ArrayList<T>, @param:LayoutRes private val layoutRes: Int) : NullableAdapter<RecyclerView.ViewHolder>() {

    abstract fun bind(itemView: View, data: T,position: Int)

    override fun onCreateViewHolderLike(parent: ViewGroup, viewType: Int): JustViewHolder = JustViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))


    override fun onBindViewHolderLike(holder: RecyclerView.ViewHolder, position: Int) {
        bind(holder.itemView,mDatas[position],position)
    }

    override fun getItemCountLike(): Int {
        return mDatas.size
    }

    inner class JustViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            click(itemView) {
                itemClick?.invoke(itemView.id, adapterPosition,mDatas[adapterPosition])
            }

        }
    }

    var itemClick: ((viewId: Int, position: Int,data:T) -> Unit)? = null

    fun setOnItemClick(click: ((viewId: Int, position: Int,data:T) -> Unit)) {
        itemClick = click
    }

    //    下面是刷新部分的代码
    ////////////////////////////////////////////
    var mSrRefresh: SwipeRefreshLayout? = null
    private var canRefresh = false
    private var refreshIng = false
    private var page = 1
    /**
     * 没有数据时显示的布局
     */
    override fun getNotDataLayout(): Int {
        return R.layout.item_just_no_data
    }

    /**
     * 加载更多的布局
     */
    override fun getFooterLayout(): Int {
        return if (canRefresh) R.layout.item_just_load_more else 0
    }

    /**
     * 设置开启加载更多
     */
    fun setSwipeRefreshLayout(srRefresh: SwipeRefreshLayout, load: (page: Int) -> Unit): JustAdapter<T> {
        mSrRefresh = srRefresh
        canRefresh = true
        mSrRefresh?.setOnRefreshListener {
            page = 1
            load(load)
        }
        onRefresh={
            if (mSrRefresh == null || !mSrRefresh!!.isRefreshing) {
                page++
                load(load)
            }
        }
        return this
    }

    /**
     * 加载的数据
     */
    fun setLoadData(datas: List<T>?, more: Boolean = true) {
        if (datas != null) {
            hideFootView = if (more) {
                datas.size < HttpManager.PAGE_SIZE
            } else {
                true
            }
            mDatas.addAll(datas)
        }
        refreshIng = false
        mSrRefresh?.isRefreshing = false
        notifyDataSetChanged()
    }
    /**
     * 设置新数据(清空原来的数据)
     */
    fun setNewData(datas: List<T>?, canMore: Boolean = true) {
        if (datas != null) {
            hideFootView = if (canMore) {
                datas.size < HttpManager.PAGE_SIZE
            } else {
                true
            }
            mDatas.clear()
            mDatas.addAll(datas)
        } else {
            mDatas.clear()
        }
        refreshIng = false
        mSrRefresh?.isRefreshing = false
        notifyDataSetChanged()
    }

    /**
     * 加载更多的回调
     */
    private fun load(load: (page: Int) -> Unit) {
        if (refreshIng) return
        refreshIng = true
        if (page == 1) {
            mDatas.clear()
        }
        load.invoke(page)
    }

    ///////////////////////////////////////////
}
