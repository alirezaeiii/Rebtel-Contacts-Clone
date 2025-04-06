package com.sample.android.contact.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sample.android.contact.domain.MainPagerItem;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private final List<MainPagerItem> fragments;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragments = new ArrayList<>();
    }

    public void addItem(MainPagerItem mainPagerItem) {
        fragments.add(mainPagerItem);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }
}
