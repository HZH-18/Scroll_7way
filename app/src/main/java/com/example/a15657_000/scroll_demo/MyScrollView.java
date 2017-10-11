package com.example.a15657_000.scroll_demo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by 15657_000 on 2017/9/12 0012.
 */

public class MyScrollView extends View {

    private int lastX,lastY;
    private int xx=0,yy=0;//这个是进行ScrollTo时整个屏幕的偏移量
    private Scroller scroller;


    public MyScrollView(Context context) {
        super(context);
        scroller = new Scroller(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            ((View)getParent()).scrollTo(scroller.getCurrX(),
                                        scroller.getCurrY());
            invalidate();
            //使用invalidate()的原因：computeScroll()只有在调用onDraw的时候才会被调用 通过invalidate()重新调用OnDraw方法 形成 invalidate()->onDraw()->computeScroll()的循环 当scroller.computeScrollOffset()为false时停止循环
        }
    }


    //使用绝对坐标要重置last的原因是：layout方法中的getLeft等方法获取的是相对坐标系的距离
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //int x = (int) event.getX();
        //int y = (int) event.getY();
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        //Log.d("view", "onTouchEvent: rawX:"+rawX +" rawY:"+rawY);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = rawX;
                lastY = rawY;
                //Log.d("view", "onTouchEvent: lastX:"+lastX +" lastY:"+lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = rawX-lastX;
                int offsetY = rawY-lastY;
                xx+=offsetX;
                yy+=offsetY;
              //  Log.d("view", "onTouchEvent: offsetX:"+offsetX +" offsetY:"+offsetY);

                //第一种方式 上下左右+偏移量
                layout(getLeft()+offsetX,getTop()+offsetY,getRight()+offsetX,getBottom()+offsetY);

                //第二种方式 封装好的方法，和上面的意思一样
               // offsetLeftAndRight(offsetX);
                //offsetTopAndBottom(offsetY);

                //第三种方式 通过LayoutParams的margin属性来修改位置
              /*  LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
                lp.leftMargin = getLeft()+offsetX;
                lp.topMargin = getTop()+offsetY;
                setLayoutParams(lp);*/
                //和上面的原理一样 但更简单
               /* ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                lp.leftMargin = getLeft()+offsetX;
                lp.topMargin = getTop()+offsetY;
                setLayoutParams(lp);*/

                //第四种方法 scrollBy和scrollTo方法 By是在移动前的基础上加上偏移量 To则是直接移动到指定的坐标 但这参数并不是坐标 也是偏移量 但是是相对于一开始而言的整个屏幕的偏移量 与上面3种方法不同的是 这两个方法里面的参数是正负取反的 因为它移动的其实不是自身的View 而是View后面的背景 所以它参照的坐标轴也不是View的左上角 而是屏幕的左上角 所以当你输入正的偏移量的时候 它在屏幕原来的坐标基础上 给屏幕的坐标添加了偏移量 这样就有了你往下偏移 但View却会往上走的现象 其实View没动 动的是屏幕或者说是背景 所以要达到期望的效果的话 偏移量或者坐标要取反 然后呢 因为这两个方法操作的不是View本身 而是View的所有子View 所以要通过父View来实现移动自身的效果
               // ((View)(getParent())).scrollBy(-offsetX,-offsetY);
                //((View)(getParent())).scrollTo(-xx,-yy);
                lastX = rawX;
                lastY = rawY;
                break;
            /*case MotionEvent.ACTION_UP:
                //设置当松开鼠标时的平移动作
                View viewGroup = ((View)getParent());
                //父ViewXY轴初始坐标，XY轴偏移量
                scroller.startScroll(viewGroup.getScrollX(),
                                    viewGroup.getScrollY(),
                                    100,
                                    100);
                //Log.d("view", "onTouchEvent: viewGroup.getScrollX():"+viewGroup.getScrollX() +" viewGroup.getScrollY():"+viewGroup.getScrollY());
                invalidate();
                break;*/
            default:
                break;
        }
        return true;
    }
}
