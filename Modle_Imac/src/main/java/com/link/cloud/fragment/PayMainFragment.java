package com.link.cloud.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.link.cloud.R;
import com.orhanobut.logger.Logger;

import com.link.cloud.constant.Constant;
import com.link.cloud.core.BaseFragment;


import java.util.ArrayList;

import butterknife.Bind;

public class PayMainFragment extends BaseFragment {
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private Handler mHandler;
    SectionsPagerAdapter mSectionsPagerAdapter;
    private ArrayList<Fragment> fragments;
    private PayFragment_One payFragment;

    public static PayMainFragment newInstance(Fragment... fragments) {
        PayMainFragment fragment = new PayMainFragment();
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
        mHandler = new Handler();
        fragments = new ArrayList<Fragment>();
        payFragment = PayFragment_One.newInstance();
        fragments.add(payFragment);

        initAdapter();
    }

    private void initAdapter() {
        if (mSectionsPagerAdapter == null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        }
        viewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void initListeners() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
//                    payFragment.etPhoneNum.getText().clear();

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0) {
                    Logger.e("onPageScrollStateChanged prevPosition:" + prevPosition);
                    if (prevPosition > 0) {
                        mSectionsPagerAdapter.destroyItem(prevPosition);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onVisible() {
//        payFragment.etPhoneNum.getText().clear();
//        payFragment.etCost.getText().clear();
//        payFragment.etRemark.getText().clear();
    }

    @Override
    protected void onInvisible() {
        if (fragments.size() > 1) {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onDestroy() {
        mHandler = null;
        super.onDestroy();
    }

    public void addFragment(Fragment fragment, int index) {
        fragments.add(index, fragment);
        mSectionsPagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(index);
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
                super.destroyItem(viewPager, position, fragments.get(position));
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
