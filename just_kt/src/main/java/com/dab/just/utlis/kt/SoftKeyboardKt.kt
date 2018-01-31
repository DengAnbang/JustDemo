package com.dab.just.utlis.kt

import android.graphics.Rect
import android.view.View

/**
 * Created by dab on 2018/1/24 10:23
 */
/**
 * @param root
 * 最外层布局，需要调整的布局
 * @param scrollToView
 * 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
 */
fun controlKeyboardLayout(root: View, scrollToView: View): () -> Unit {
    // 注册一个回调函数，当在一个视图树中
    // 全局布局发生改变或者视图树中的某个视图的可视状态发生改变时调用这个回调函数。
    var top = 0
    val function = {
        val rect = Rect()
        // 获取root在窗体的可视区域
        root.getWindowVisibleDisplayFrame(rect)
        // 当前视图最外层的高度减去现在所看到的视图的最底部的y坐标
        val rootInvisibleHeight = root.height - rect.bottom
        // 若rootInvisibleHeight高度大于100，则说明当前视图上移了，说明软键盘弹出了
        if (rootInvisibleHeight > 100) {
            if (top == 0) {
                top= scrollToView.top
            }
            //软键盘弹出来的时候
            val location = IntArray(2)
            // 获取scrollToView在窗体的坐标
            scrollToView.getLocationInWindow(location)
            // 计算root滚动高度，使scrollToView在可见区域的底部
            val srollHeight = location[1] + scrollToView.height - rect.bottom
            scrollToView.layout(scrollToView.left, scrollToView.top-srollHeight,scrollToView.right,scrollToView.bottom-srollHeight)
//            scrollToView.scrollBy(0, -srollHeight)
        } else if (top!=0) {
            scrollToView.layout(scrollToView.left, top,scrollToView.right,scrollToView.bottom)
            // 软键盘没有弹出来的时候
//            scrollToView.scrollTo(0, 0)
        }
    }
    root.viewTreeObserver.addOnGlobalLayoutListener(function)
    return {
        root.viewTreeObserver.removeOnGlobalLayoutListener(function)
    }
}