package com.sample.android.contact.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sample.android.contact.model.MainPagerItem;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<MainPagerItem> fragments = new ArrayList<>();

    MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    void addItem(MainPagerItem mainPagerItem) {
        fragments.add(mainPagerItem);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).title;
    }
}
