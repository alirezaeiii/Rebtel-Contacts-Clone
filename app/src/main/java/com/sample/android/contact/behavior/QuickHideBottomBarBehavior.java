package com.sample.android.contact.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

import com.sample.android.contact.R;

public class QuickHideBottomBarBehavior extends QuickHideBehavior {

    //Required to instantiate as a default behavior
    @SuppressWarnings("unused")
    public QuickHideBottomBarBehavior() {
    }

    //Required to attach behavior via XML
    @SuppressWarnings("unused")
    public QuickHideBottomBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void directionUpScrolling(View recyclerView) {
        MarginLayoutParams params = (MarginLayoutParams) recyclerView.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        recyclerView.setLayoutParams(params);
    }

    @Override
    protected void directionDownScrolling(View recyclerView, View child) {
        MarginLayoutParams params = (MarginLayoutParams) recyclerView.getLayoutParams();
        params.setMargins(0, 0, 0,
                (int) child.getContext().getResources().getDimension(R.dimen.dimen_recycler_view_spacing));
        mHandler.postDelayed(() -> recyclerView.setLayoutParams(params), 250);
    }
}
