package com.sample.android.contact.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sample.android.contact.R;
import com.sample.android.contact.domain.MainPagerItem;
import com.sample.android.contact.util.TabIndicatorFollower;
import com.sample.android.contact.widget.ListenableTabLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String SELECTED_ITEM = "selected_item";

    @Inject
    ContactsFragment contactsFragment;

    @Inject
    DialpadFragment dialpadFragment;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    ListenableTabLayout mTabLayout;

    @BindView(R.id.triangle)
    View mTriangle;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addItem(new MainPagerItem(contactsFragment, getString(R.string.app_name)));
        pagerAdapter.addItem(new MainPagerItem(dialpadFragment, getString(R.string.dialpad)));

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        TabIndicatorFollower.setupWith(mTabLayout, mTriangle);

        if (savedInstanceState != null) {
            TabLayout.Tab tab = mTabLayout.getTabAt(savedInstanceState.getInt(SELECTED_ITEM));
            tab.select();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM, mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        unbinder.unbind();
    }
}