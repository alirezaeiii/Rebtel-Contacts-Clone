package com.sample.android.contact;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    ListenableTabLayout mTabLayout;

    @BindView(R.id.triangle)
    View mTriangle;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addItem(new MainPagerItem(new ContactsFragment(), getString(R.string.app_name)));
        pagerAdapter.addItem(new MainPagerItem(new DialpadFragment(), getString(R.string.Dialpad)));

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        TabIndicatorFollower.setupWith(mTabLayout, mTriangle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}