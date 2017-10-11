package com.example.a15657_000.scroll_demo;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by 15657_000 on 2017/9/12 0012.
 */

public class DragView extends FrameLayout {

    private ViewDragHelper viewDragHelper;
    private View mMain,mMenu;
    private int mWidth;


    //当xml被解析的时候调用 一般在activity中的setContentView()之后onMeasure()前被调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMain = getChildAt(1);
        mMenu = getChildAt(0);
    }

    public DragView(Context context) {
        super(context);
        init();
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = mMenu.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    public void init(){
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //Log.d("child", "tryCaptureView: "+child +" mMain:"+mMain + " mMenu:"+mMenu);
            return mMain == child;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(mMain.getLeft() < 500){
                viewDragHelper.smoothSlideViewTo(mMain,0,0);
                ViewCompat.postInvalidateOnAnimation(DragView.this);
            }
            else
            {
                viewDragHelper.smoothSlideViewTo(mMain,300,0);
                ViewCompat.postInvalidateOnAnimation(DragView.this);
            }
        }
    };

    @Override
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
