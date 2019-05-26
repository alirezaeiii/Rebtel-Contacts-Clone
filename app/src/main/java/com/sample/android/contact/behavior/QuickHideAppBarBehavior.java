package com.sample.android.contact.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.sample.android.contact.R;

public class QuickHideAppBarBehavior extends QuickHideBehavior {

    //Required to instantiate as a default behavior
    @SuppressWarnings("unused")
    public QuickHideAppBarBehavior() {
    }

    //Required to attach behavior via XML
    @SuppressWarnings("unused")
    public QuickHideAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void directionUpScrolling(View recyclerView) {
        recyclerView.setPadding(0, 0, 0, 0);
    }

    @Override
    protected void directionDownScrolling(View recyclerView, View child) {
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = child.getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true) ?
                TypedValue.complexToDimensionPixelSize(tv.data, child.getContext().getResources().getDisplayMetrics()) :
                (int) child.getContext().getResources().getDimension(R.dimen.dimen_recycler_view_spacing);
        mHandler.postDelayed(() -> recyclerView.setPadding(0, actionBarHeight, 0, 0), 250);
    }
}
