package com.sample.android.contact.behavior;

import static com.sample.android.contact.util.ContactUtils.getActionBarHeight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

public class QuickHideAppBarBehavior extends QuickHideBehavior {

    private int mActionBarHeight;

    //Required to instantiate as a default behavior
    @SuppressWarnings("unused")
    public QuickHideAppBarBehavior() {
    }

    //Required to attach behavior via XML
    @SuppressWarnings("unused")
    public QuickHideAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Calculate ActionBar height
        mActionBarHeight = getActionBarHeight(context);
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                                 @NonNull View target, float velocityX, float velocityY,
                                 boolean consumed) {
        // target is instance of SwipeRefreshLayout by default and RecyclerView in Search state
        // We do not want to animate searchView when we are in Search state
        if (!(target instanceof RecyclerView)) {
            return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
        }
        return false;
    }

    @Override
    protected float getTargetHideValue(ViewGroup parent, View target) {
        return -target.getHeight();
    }

    @Override
    protected void removeSpace(View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        view.setLayoutParams(params);
    }

    @Override
    protected void addSpace(View view) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        mHandler.postDelayed(() -> {
            params.setMargins(0, mActionBarHeight, 0, 0);
            view.setLayoutParams(params);
        }, ADD_SPACE_DELAY);
    }
}
