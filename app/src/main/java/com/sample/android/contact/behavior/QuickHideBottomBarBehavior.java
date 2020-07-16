package com.sample.android.contact.behavior;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.sample.android.contact.R;

public class QuickHideBottomBarBehavior extends QuickHideBehavior {

    private Handler mHandler = new Handler(Looper.getMainLooper());
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
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                                 @NonNull View target, float velocityX, float velocityY,
                                 boolean consumed) {
        if(mRecyclerView == null) {
            mRecyclerView = target.findViewById(R.id.recyclerView);
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    protected void directionUpScrolling() {
        MarginLayoutParams params = (MarginLayoutParams) mRecyclerView.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        mRecyclerView.setLayoutParams(params);
    }

    @Override
    protected void directionDownScrolling() {
        MarginLayoutParams params = (MarginLayoutParams) mRecyclerView.getLayoutParams();
        mHandler.postDelayed(() -> {
            params.setMargins(0, 0, 0, bottomSpacing);
            mRecyclerView.setLayoutParams(params);
        }, 250);
    }

    @Override
    protected float getTargetHideValue(ViewGroup parent, View target) {
        return parent.getHeight() - target.getTop();
    }
}
