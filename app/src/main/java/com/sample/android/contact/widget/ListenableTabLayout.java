package com.sample.android.contact.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ListenableTabLayout extends TabLayout {

    private ViewPager viewPager;
    private final List<OnAddedToViewPager> onAddedToViewPagerListeners = new ArrayList<>();

    public ListenableTabLayout(Context context) {
        super(context);
    }

    public ListenableTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenableTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addOnAddedToViewPagerListener(OnAddedToViewPager listener) {
        onAddedToViewPagerListeners.add(listener);
        if(viewPager != null){
            listener.onAddedToViewPager(this, viewPager);
        }
    }

    public void removeOnAddedToViewPagerListener(OnAddedToViewPager listener) {
        onAddedToViewPagerListeners.remove(listener);
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
        this.viewPager = viewPager;
        for (OnAddedToViewPager onAddedToViewPagerListener : onAddedToViewPagerListeners) {
            onAddedToViewPagerListener.onAddedToViewPager(this, viewPager);
        }
    }

    public interface OnAddedToViewPager {
        void onAddedToViewPager(ListenableTabLayout tabLayout, ViewPager viewPager);
    }
}
