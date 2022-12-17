package com.sample.android.contact.ui.contact;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sample.android.contact.R;
import com.sample.android.contact.domain.MainPagerItem;
import com.sample.android.contact.ui.adapter.MainPagerAdapter;
import com.sample.android.contact.util.TabIndicatorFollower;
import com.sample.android.contact.widget.ListenableTabLayout;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    ListenableTabLayout mTabLayout;

    @BindView(R.id.triangle)
    View mTriangle;

    private Unbinder unbinder;

    private int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addItem(new MainPagerItem(new ContactsFragment(), getString(R.string.contacts)));
        pagerAdapter.addItem(new MainPagerItem(new DialpadFragment(), getString(R.string.dialpad)));

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        TabIndicatorFollower.setupWith(mTabLayout, mTriangle);
        mHandler.postDelayed(() -> mTabLayout.getTabAt(0).select(), 10);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mTriangle.setPressed(false);
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

                private static final int MAX_CLICK_DURATION = 105;
                private long startClickTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (selectedPosition == tabIndex) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            startClickTime = Calendar.getInstance().getTimeInMillis();
                            setTrianglePressed(true, 100);
                        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                            mTriangle.setPressed(false);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                            if (clickDuration < MAX_CLICK_DURATION) {
                                mHandler.removeCallbacksAndMessages(null);
                                mTriangle.setPressed(true);
                                setTrianglePressed(false, 50);
                            } else {
                                mTriangle.setPressed(false);
                            }
                        } else mTriangle.setPressed(v.isPressed());
                    }
                    return false;
                }
            });
        }
    }

    private void setTrianglePressed(boolean isPressed, int delay) {
        mHandler.postDelayed(() -> mTriangle.setPressed(isPressed), delay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}