package com.link.cloud.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.link.cloud.R;

import java.util.ArrayList;
import java.util.List;



public class TabViewPager extends LinearLayout {
    /* 数据段begin */
    public final static String TAG = "TabViewPager";
    private Context mContext;
    private boolean noScroll = false;
    private RadioGroup mTabHost;
    private List<RadioButton> rbList;
    private ImageView mUpline;
    private NoScrollViewPager mViewPager;

    //tab及underline宽度，也是underline的最小移动距离
    private int mTabWidth, tabs;
     /* 数据段end */

    /* 函数段begin */
    public TabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        inflate(mContext, R.layout.tab_view_pager, this);
        initViews();
    }

    private void initViews() {
        mTabHost = (RadioGroup) findViewById(R.id.tab_host);
        mUpline = (ImageView) findViewById(R.id.tab_upline);
        mViewPager = (NoScrollViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(event);
    }

    public void initTabs(int[] iconRes, String[] tabTitles, int parentWidth) {
        LayoutParams tabHostLayoutParams;
        LayoutParams params;
        //减去分割线宽度
        mTabWidth = (parentWidth - (tabTitles.length - 1) * tabTitles.length) / tabTitles.length;
        //设置宽度
        if (tabTitles.length > 0) {
            tabs = tabTitles.length;
            rbList = new ArrayList<RadioButton>(tabs);

            tabHostLayoutParams = new LayoutParams(mTabWidth, LayoutParams.MATCH_PARENT);
            tabHostLayoutParams.weight = 1;
            tabHostLayoutParams.gravity = Gravity.CENTER;

            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
        } else {
            return;
        }

        //动态添加tab
        RadioButton radioButton;
        ImageView spitLine = null;
        for (int loopVal = 0; loopVal < tabTitles.length; loopVal++) {
            radioButton = (RadioButton) inflate(mContext, R.layout.radiobutton, null);
            radioButton.setText(tabTitles[loopVal]);
            if (loopVal == 0)
                radioButton.setChecked(true);
            Drawable nav_left = getResources().getDrawable(iconRes[loopVal]);
            nav_left.setBounds(0, 0, nav_left.getMinimumWidth(), nav_left.getMinimumHeight());
            radioButton.setCompoundDrawables(nav_left, null, null, null);
            radioButton.setOnClickListener(new TabOnClickListener(loopVal));
            radioButton.setShadowLayer(2, 1, 1, Color.BLACK);
            rbList.add(radioButton);
            mTabHost.addView(radioButton, tabHostLayoutParams);

            if (loopVal < (tabTitles.length - 1)) {
                spitLine = (ImageView) inflate(mContext, R.layout.vertical_line, null);
                mTabHost.addView(spitLine);
            }
        }

        //设置upline宽度，使得下划线与tab宽度保持一致
        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(mTabWidth, 5);
        frameLayoutParams.gravity = Gravity.TOP;
        mUpline.setLayoutParams(frameLayoutParams);
        mUpline.setBackgroundDrawable(getResources().getDrawable(R.drawable.upline));
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        mViewPager.setAdapter(pagerAdapter);
        //滑动viewPager时也要执行mUnderline的移动动画
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private boolean left = false;
            private boolean right = false;
            private boolean isScrolling = false;
            private int lastValue = -1;

            private RadioButton prevRB, currentRB;
            private int currentPosition = 0;
            private int prevPosition = 0;

            @Override
            public void onPageSelected(int position) {
                prevPosition = currentPosition;
                currentPosition = position;
                /*
                if (BaseApplication.DEBUG)
                    Logger.e("onPageSelected" + "[currentPosition:" + currentPosition + ";prevPosition:" + prevPosition + "]");
                */
                prevRB = rbList.get(prevPosition);
                currentRB = rbList.get(currentPosition);

                prevRB.setChecked(false);
                currentRB.setChecked(true);

                //mUnderline的移动动画
                mUpline.startAnimation(new UnderlineTranslateAnimation(prevRB.getX(), currentRB.getX(), 0, 0));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (isScrolling) {
                    if (lastValue > positionOffsetPixels) {
                        // 递减，向右侧滑动
                        right = true;
                        left = false;
                    } else if (lastValue < positionOffsetPixels) {
                        // 递减，向右侧滑动
                        right = false;
                        left = true;
                    } else if (lastValue == positionOffsetPixels) {
                        right = left = false;
                    }
                }
                lastValue = positionOffsetPixels;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    isScrolling = true;
                } else if (state == 2) {
                    right = left = false;
                } else {
                    isScrolling = false;
                }
            }
        });
    }
    public void setCurrentItem(int position) {
        //记录当前的位置后再设置选中位置
        int currentPosition = mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(position);
        int nextPosition = mViewPager.getCurrentItem();

        //mUnderline的移动动画
        //mUpline.startAnimation(new UnderlineTranslateAnimation(currentPosition * mTabWidth, nextPosition * mTabWidth, 0, 0));
    }
     /* 函数段end */

    public int getCurrentPosition() {
        return mViewPager.getCurrentItem();
    }

    /* 内部类begin */
    private class TabOnClickListener implements OnClickListener {
        private int viewPosition = -1;

        public TabOnClickListener(int position) {
            viewPosition = position;
        }

        @Override
        public void onClick(View v) {
            setCurrentItem(viewPosition);
        }
    }

    private class UnderlineTranslateAnimation extends TranslateAnimation {
        public UnderlineTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
            super(fromXDelta, toXDelta, fromYDelta, toYDelta);
            /*
            if (BaseApplication.DEBUG)
                Logger.e("UnderlineTranslateAnimation" + "[fromXDelta:" + fromXDelta + ";toXDelta:" + toXDelta + "]");
            */
            setFillAfter(true);
        }

    }
     /* 内部类end */
}