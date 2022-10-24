package com.sample.android.contact.ui.contact;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sample.android.contact.R;
import com.sample.android.contact.domain.MainPagerItem;
import com.sample.android.contact.ui.adapter.MainPagerAdapter;
import com.sample.android.contact.util.TabIndicatorFollower;
import com.sample.android.contact.widget.ListenableTabLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

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

    private int mSelectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectedPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        final View tabStrip = mTabLayout.getChildAt(0);
        int childCount = ((ViewGroup) tabStrip).getChildCount();
        for (int i = 0; i < childCount; i++) {
            View tabView = ((ViewGroup) tabStrip).getChildAt(i);
            final int tabIndex = i;
            tabView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mSelectedPosition == tabIndex) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            mHandler.postDelayed(() -> mTriangle.setPressed(true), 250);
                        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                            mHandler.postDelayed(() -> mTriangle.setPressed(false), 120);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            mHandler.postDelayed(() -> mTriangle.setPressed(false), 120);
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}