package com.dab.just.custom;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by DAB on 2016/7/31 12:13.
 * 滑动删除的View
 */
public class SwipeDelete extends FrameLayout {

    private View mContentView;//内容区
    private View mDeleteView;//删除区
    private int mDeleteHeight;//删除区的高度
    private int mDeleteWidth;//删除区的宽度
    private int mContentWidth;//内容区的宽
    private ViewDragHelper mViewDragHelper;
    private boolean isThisOpen;//当前这个是否打开
    private static boolean isOtherOpen;//有没有其他的打开
    private boolean isSliding = true;//设置是否允许滑动
    private boolean isOnlyOneSlide = true;//设置是否只有一个允许滑动

    /**
     * 设置是否能滑动
     *
     * @param sliding
     */
    public void setSliding(boolean sliding) {
        isSliding = sliding;
    }

    /**
     * 设置是否允许滑动多个
     *
     * @param onlyOneSlide
     */
    public void setOnlyOneSlide(boolean onlyOneSlide) {
        isOnlyOneSlide = onlyOneSlide;
    }

    /**
     * 滑动打开了的那一个
     */
    public static SwipeDelete getSwipe;
    public SwipeDelete(Context context) {
        super(context);
        init();
    }

    public SwipeDelete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeDelete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, mCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mDeleteView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDeleteHeight = mDeleteView.getMeasuredHeight();
        mDeleteWidth = mDeleteView.getMeasuredWidth();
        mContentWidth = mContentView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    private float downX, downY;//按下的时候坐标

    @Override
    public boolean onTouchEvent(MotionEvent event) {


//如果有其他的打开,并且不是当前滑动的这个
        if (isOnlyOneSlide) {
            if (isOtherOpen && !isThisOpen) {
                getSwipe.close();
                return true;
            }
        }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float moveX = event.getX();
                    float moveY = event.getY();
                    float delatX = moveX - downX;//x方向移动的距离
                    float delatY = moveY - downY;//y方向移动的距离
                    //移动偏向水平的
                    if (Math.abs(delatX) > Math.abs(delatY)) {
                        requestDisallowInterceptTouchEvent(true);//请求自己处理,父控件不处理
                    }
                    downX = moveX;
                    downY = moveY;
                    break;
                case MotionEvent.ACTION_UP:
                    break;

            }

            mViewDragHelper.processTouchEvent(event);


        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mContentView.layout(0, 0, mContentWidth, mDeleteHeight);
        mDeleteView.layout(mContentView.getRight(), 0, mContentView.getRight() + mDeleteWidth, mDeleteHeight);
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return isSliding && (child == mContentView || child == mDeleteView);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {

            return mDeleteWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            if (child == mContentView) {
                if (left > 0) left = 0;
//                if (left < -AutoUtils.scaleValue(mDeleteWidth)) left = -AutoUtils.scaleValue(mDeleteWidth);
                if (left < -mDeleteWidth) left = -mDeleteWidth;
            } else if (child == mDeleteView) {
                if (left > mContentView.getWidth()) left = mContentView.getWidth();
                if (left < mContentWidth - mDeleteWidth) left = mContentWidth - mDeleteWidth;
            }
            return left;

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == mContentView) {
                mDeleteView.layout(mDeleteView.getLeft() + dx, mDeleteView.getTop() + dy, mDeleteView.getRight() + dx, mDeleteView.getBottom() + dy);
            } else if (changedView == mDeleteView) {
                mContentView.layout(mContentView.getLeft() + dx, mContentView.getTop() + dy, mContentView.getRight() + dx, mContentView.getBottom() + dy);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (mContentView.getLeft() <= -mDeleteWidth / 2) {
                open();
            } else {
                close();
            }
        }
    };

    /**
     * 关闭删除区
     */
    public void close() {
        mViewDragHelper.smoothSlideViewTo(mContentView, 0, mContentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeDelete.this);
        isThisOpen = false;
        isOtherOpen = false;
    }

    /**
     * 打开删除区
     */
    public void open() {
        mViewDragHelper.smoothSlideViewTo(mContentView, -mDeleteWidth, mContentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeDelete.this);
        getSwipe = this;
        isThisOpen = true;
        isOtherOpen = true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true))
            ViewCompat.postInvalidateOnAnimation(SwipeDelete.this);
    }



}
