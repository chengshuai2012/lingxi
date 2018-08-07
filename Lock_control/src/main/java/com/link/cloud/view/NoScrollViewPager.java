package com.link.cloud.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/7/25.
 */

public class NoScrollViewPager extends LazyViewPager {
    private boolean isCanScroll=false;
    public NoScrollViewPager (Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public NoScrollViewPager(Context context){
        super(context);
    }
    public void setNoScroll(boolean isCanScroll){
        this.isCanScroll=isCanScroll;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return isCanScroll&&super.onTouchEvent(arg0);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }
}
