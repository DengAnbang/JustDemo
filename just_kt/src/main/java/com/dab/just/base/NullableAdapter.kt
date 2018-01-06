package com.dab.just.base

import android.content.ContentValues.TAG
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by dab on 2018/1/5 0005 17:48
 */
abstract class NullableAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_NO_DATA = 0x9574
    private val TYPE_FOOTER = 0x9575
    var hideFootView = false //不再显示脚布局(比如加载更多的时候,没有更多的数据了,就可以隐藏掉)
    var DEBUG = false
    open var onRefresh: (()-> Unit )?=null

    abstract fun onCreateViewHolderLike(parent: ViewGroup, viewType: Int): VH

    abstract fun onBindViewHolderLike(holder: VH, position: Int)

    abstract fun getItemCountLike(): Int

    open fun onBindViewHolderLike(holder: NoDataHolder, position: Int) {}

    open fun getItemViewTypeLike(position: Int): Int =0

    @LayoutRes
    open fun getNotDataLayout(): Int =0

    @LayoutRes
    open fun getFooterLayout(): Int =0

    @CallSuper
    open fun onBindViewHolderLike(holder: FooterHolder, position: Int) {
        holder.itemView.visibility = if (hideFootView) View.GONE else View.VISIBLE
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoDataHolder ->onBindViewHolderLike(holder, position)
            is FooterHolder ->onBindViewHolderLike(holder, position)
            else -> onBindViewHolderLike( holder as VH, position)
        }

    }

    override fun getItemCount(): Int {
        var count = 0
        val itemCountLike = getItemCountLike()
        do {
            if (itemCountLike == 0) {
                //判断是否需要显示无数据布局
                if (getNotDataLayout() != 0) {
                    count = 1
                } else {
                    if (DEBUG) Log.e(TAG, " getNotDataLayout(): 未重写,跳过布局")
                }
                //直接不判断是否还有脚布局
                break
            }
            count = itemCountLike
            //有脚布局
            if (getFooterLayout() != 0) {
                count += 1
            }

        } while (false)
        return count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_NO_DATA) {
            return NoDataHolder(LayoutInflater.from(parent.context).inflate(getNotDataLayout(), parent, false))
        }
        return if (viewType == TYPE_FOOTER) {
            FooterHolder(LayoutInflater.from(parent.context).inflate(getFooterLayout(), parent, false))
        } else onCreateViewHolderLike(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        val viewType: Int
        val itemCountLike = getItemCountLike()
        viewType = when {
            //判断是否需要显示无数据布局
            position == 0 && getNotDataLayout() != 0 && itemCountLike == 0 -> TYPE_NO_DATA
            //判断是否需要显示角布局
            position == itemCountLike && getFooterLayout() != 0 && itemCountLike > 0 -> TYPE_FOOTER
            else -> getItemViewTypeLike(position)
        }
        return viewType
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return
                val layoutManager = recyclerView!!.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (lastCompletelyVisibleItemPosition + 1 == itemCount&&!hideFootView) {
                        onRefresh?.invoke()
                    }
                } else {
                    if (DEBUG)
                        Log.e(TAG, " 未找到:mLastCompletelyVisibleItemPosition,如果不是用LinearLayoutManager,需要传入mLastCompletelyVisibleItemPosition")
                    return
                }


            }
        })
    }

     class NoDataHolder(inflate: View) : RecyclerView.ViewHolder(inflate)
     class FooterHolder(inflate: View) : RecyclerView.ViewHolder(inflate)
}