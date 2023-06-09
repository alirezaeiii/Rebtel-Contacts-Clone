package com.sample.android.contact.widget;

import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

public class TabIndicatorFollower {

    private ListenableTabLayout tabLayout;
    private View indicatorView;
    private ViewPager viewPager;

    public static TabIndicatorFollower setupWith(ListenableTabLayout tabLayout, View indicatorView){
        return new TabIndicatorFollower().setTabLayout(tabLayout).setIndicatorView(indicatorView);
    }

    public TabIndicatorFollower() {
    }

    public TabIndicatorFollower setTabLayout(ListenableTabLayout listenableTabLayout){
        if(this.tabLayout != null){
            this.tabLayout.removeOnAddedToViewPagerListener(onAddedToViewPager);
        }
        this.tabLayout = listenableTabLayout;
        this.tabLayout.addOnAddedToViewPagerListener(onAddedToViewPager);
        return this;
    }

    public TabIndicatorFollower setIndicatorView(View view){
        this.indicatorView = view;
        return this;
    }

    private final ListenableTabLayout.OnAddedToViewPager onAddedToViewPager = new ListenableTabLayout.OnAddedToViewPager() {
        @Override
        public void onAddedToViewPager(ListenableTabLayout tl, ViewPager vp) {
            viewPager = vp;
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if(indicatorView != null && tabLayout != null && viewPager != null) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                        if (tabLayout.getTabCount() == 0) {
                            return;
                        }

                        final View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(position);
                        new Handler(Looper.getMainLooper()).post(() -> {
                            final int tabWidth = tabView.getMeasuredWidth();

                            float x = tabView.getLeft() + tabWidth * positionOffset;
                            x += (tabWidth / 2f - indicatorView.getWidth() / 2f);

                            indicatorView.setX(x);
                        });
                    }
                }
            });
        }
    };
}
