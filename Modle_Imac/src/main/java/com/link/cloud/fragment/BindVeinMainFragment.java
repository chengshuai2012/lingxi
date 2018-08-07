package com.link.cloud.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.link.cloud.BuildConfig;
import com.link.cloud.R;
import com.orhanobut.logger.Logger;

import com.link.cloud.constant.Constant;
import com.link.cloud.core.BaseFragment;
import com.link.cloud.view.NoScrollViewPager;


import java.util.ArrayList;

import butterknife.Bind;

public class BindVeinMainFragment extends BaseFragment {

    @Bind(R.id.viewPager)
    NoScrollViewPager bindViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    ArrayList<Fragment> fragments;

    private RegisterFragment_One registerFragment;

    public static BindVeinMainFragment newInstance(Fragment... fragments) {
        BindVeinMainFragment fragment = new BindVeinMainFragment();
        Bundle args = new Bundle();
        ArrayList<Fragment> lsit = new ArrayList<Fragment>();
        for (Fragment frag : fragments) {
            lsit.add(frag);
        }
        args.putSerializable(Constant.EXTRAS_FRAGMENT, lsit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_viewpager;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        fragments = new ArrayList<Fragment>();
        registerFragment = RegisterFragment_One.newInstance();
        fragments.add(registerFragment);
        initAdapter();
    }

    private void initAdapter() {
        if (mSectionsPagerAdapter == null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        }
        bindViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void initListeners() {
        bindViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentPosition = 0;
            private int prevPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                prevPosition = currentPosition;
                currentPosition = position;
                if (currentPosition == 0) {
                    //重置电话号码与验证码
//                    registerFragment.etPhoneNum.setText("");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
//                    Logger.e("onPageScrollStateChanged prevPosition:" + prevPosition);
                    if (prevPosition > 0) {
                        mSectionsPagerAdapter.destroyItem(prevPosition);
                    }
                }
            }
        });
    }
    @Override
    protected void initData() {
    }
    @Override
    protected void onVisible() {
        if (BuildConfig.DEBUG) Logger.e("BindVeinMainFragment->onVisible");
        registerFragment.setUserVisibleHint(true);
    }

    @Override
    protected void onInvisible() {
//        if (BuildConfig.DEBUG) Logger.e("BindVeinMainFragment->onInvisible");
//        if (fragments.size() > 1) {
//            bindViewPager.setCurrentItem(0);
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void addFragment(Fragment fragment, int index) {
        fragments.add(index, fragment);
        mSectionsPagerAdapter.notifyDataSetChanged();
        bindViewPager.setCurrentItem(index);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (fragments != null)
                return fragments.get(position);
            else
                return null;
        }

        public void destroyItem(int position) {
            if (position < fragments.size()) {
                super.destroyItem(bindViewPager, position, fragments.get(position));
                fragments.remove(position);
                this.notifyDataSetChanged();
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            if (fragments != null)
                return fragments.size();
            else
                return 0;
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }
}
