package com.sample.android.contact.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

import com.sample.android.contact.R;

/**
 * Simple scrolling behavior that monitors nested events in the scrolling
 * container to implement a quick hide/show for the attached view.
 */
public class QuickHideBehavior extends CoordinatorLayout.Behavior<View> {

    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_DOWN = -1;

    /* Tracking last threshold crossed */
    private int mScrollTrigger;

    private ObjectAnimator mAnimator;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    //Required to instantiate as a default behavior
    @SuppressWarnings("unused")
    public QuickHideBehavior() {
    }

    //Required to attach behavior via XML
    @SuppressWarnings("unused")
    public QuickHideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //Called before a nested scroll event. Return true to declare interest
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       View child, View directTargetChild, View target,
                                       int nestedScrollAxes, int type) {
        //We have to declare interest in the scroll to receive further events
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    //Called after the scrolling child handles the fling
    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout,
                                 View child, View target, float velocityX, float velocityY,
                                 boolean consumed) {
        View recyclerView = target.findViewById(R.id.recyclerView);
        MarginLayoutParams params = (MarginLayoutParams) recyclerView.getLayoutParams();
        //We only care when the target view is already handling the fling
        if (consumed) {
            if (velocityY > 0 && mScrollTrigger != DIRECTION_UP) {
                mScrollTrigger = DIRECTION_UP;
                restartAnimator(child, getTargetHideValue(coordinatorLayout, child));
                if(child instanceof AppBarLayout) {
                    recyclerView.setPadding(0, 0,0,0);
                } else if(child instanceof RelativeLayout) {
                    params.setMargins(0, 0, 0, 0);
                    recyclerView.setLayoutParams(params);
                }
            } else if (velocityY < 0 && mScrollTrigger != DIRECTION_DOWN) {
                mScrollTrigger = DIRECTION_DOWN;
                restartAnimator(child, 0f);
                if (child instanceof AppBarLayout) {
                    // Calculate ActionBar height
                    TypedValue tv = new TypedValue();
                    int actionBarHeight = child.getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true) ?
                            TypedValue.complexToDimensionPixelSize(tv.data, child.getContext().getResources().getDisplayMetrics()) :
                            (int) child.getContext().getResources().getDimension(R.dimen.dimen_recycler_view_spacing);
                    mHandler.postDelayed(() -> recyclerView.setPadding(0, actionBarHeight, 0, 0), 250);
                } else if (child instanceof RelativeLayout) {
                    params.setMargins(0, 0, 0,
                            (int) child.getContext().getResources().getDimension(R.dimen.dimen_recycler_view_spacing));
                    mHandler.postDelayed(() -> recyclerView.setLayoutParams(params), 250);
                }
            }
        }
        return false;
    }

    /* Helper Methods */

    //Helper to trigger hide/show animation
    private void restartAnimator(View target, float value) {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }

        mAnimator = ObjectAnimator
                .ofFloat(target, View.TRANSLATION_Y, value)
                .setDuration(250);
        mAnimator.start();
    }

    private float getTargetHideValue(ViewGroup parent, View target) {
        if (target instanceof AppBarLayout) {
            return -target.getHeight();
        } else if (target instanceof RelativeLayout) {
            return parent.getHeight() - target.getTop();
        }
        return 0f;
    }
}
