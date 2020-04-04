package com.sample.android.contact.model;

import androidx.fragment.app.Fragment;

public class MainPagerItem {

    public Fragment fragment;
    public String title;

    public MainPagerItem(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }
}
