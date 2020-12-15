package com.sample.android.contact.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

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

    private final Handler mHandler = new Handler(Looper.getMainLooper());

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
        pagerAdapter.addItem(new MainPagerItem(contactsFragment, getString(R.string.contacts)));
        pagerAdapter.addItem(new MainPagerItem(dialpadFragment, getString(R.string.dialpad)));

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        TabIndicatorFollower.setupWith(mTabLayout, mTriangle);
        mHandler.postDelayed(() -> mTabLayout.getTabAt(0).select(), 10);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        unbinder.unbind();
    }
}