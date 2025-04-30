package com.sample.android.contact.ui.contact;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.sample.android.contact.R;
import com.sample.android.contact.databinding.ActivityMainBinding;
import com.sample.android.contact.domain.MainPagerItem;
import com.sample.android.contact.ui.adapter.MainPagerAdapter;
import com.sample.android.contact.widget.TabIndicatorFollower;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int TRIANGLE_DELAY_PRESS = 50;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private ActivityMainBinding mBinding;

    private int mSelectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addItem(new MainPagerItem(new ContactsFragment(), getString(R.string.contacts)));
        pagerAdapter.addItem(new MainPagerItem(new DialpadFragment(), getString(R.string.dialpad)));

        mBinding.viewPager.setAdapter(pagerAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        TabIndicatorFollower.setupWith(mBinding.tabLayout, mBinding.triangle);
        mHandler.post(() -> mBinding.tabLayout.getTabAt(0).select());

        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectedPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTrianglePressed(false, TRIANGLE_DELAY_PRESS);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        final View tabStrip = mBinding.tabLayout.getChildAt(0);
        int childCount = ((ViewGroup) tabStrip).getChildCount();
        for(int i = 0; i < childCount; i++) {
            View tabView = ((ViewGroup) tabStrip).getChildAt(i);
            final int tabIndex = i;
            tabView.setOnTouchListener(new View.OnTouchListener() {

                private static final int MAX_CLICK_DURATION = 105;
                private long startClickTime;
                private boolean isDown = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mSelectedPosition == tabIndex) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            startClickTime = Calendar.getInstance().getTimeInMillis();
                            setTrianglePressed(true, TRIANGLE_DELAY_PRESS * 2);
                            isDown = true;
                        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                            if(isDown) {
                                mHandler.removeCallbacksAndMessages(null);
                                isDown = false;
                            }
                            mBinding.triangle.setPressed(false);
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                            if (clickDuration < MAX_CLICK_DURATION) {
                                mHandler.removeCallbacksAndMessages(null);
                                mBinding.triangle.setPressed(true);
                                setTrianglePressed(false, TRIANGLE_DELAY_PRESS);
                            } else {
                                mBinding.triangle.setPressed(false);
                            }
                        } else mBinding.triangle.setPressed(v.isPressed());
                    }
                    return false;
                }
            });
        }
    }

    private void setTrianglePressed(boolean isPressed, int delay) {
        mHandler.postDelayed(() -> mBinding.triangle.setPressed(isPressed), delay);
    }
}