package com.sample.android.contact.ui;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sample.android.contact.domain.MainPagerItem;

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
