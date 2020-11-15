package com.sample.android.contact.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sample.android.contact.R;

public class QuickHideBottomBarBehavior extends QuickHideBehavior {

    private int bottomSpacing;

    //Required to instantiate as a default behavior
    @SuppressWarnings("unused")
    public QuickHideBottomBarBehavior() {
    }

    //Required to attach behavior via XML
    @SuppressWarnings("unused")
    public QuickHideBottomBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        bottomSpacing = (int) context.getResources().getDimension(R.dimen.dimen_recycler_view_spacing);
    }

    @Override
    protected float getTargetHideValue(ViewGroup parent, View target) {
        return parent.getHeight() - target.getTop();
    }

    @Override
    protected void removeSpace(View swipeRefreshLayout) {
        swipeRefreshLayout.setPadding(0, 0, 0, 0);
    }

    @Override
    protected void addSpace(View swipeRefreshLayout) {
        mHandler.postDelayed(() ->
                swipeRefreshLayout.setPadding(0, 0, 0, bottomSpacing), 200);
    }
}
