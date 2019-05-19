package com.sample.android.contact.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  View child, View target, int dx, int dy,
                                  int[] consumed, int type) {
        if (dy > 0 && mScrollTrigger != DIRECTION_UP) {
            mScrollTrigger = DIRECTION_UP;
            restartAnimator(child, getTargetHideValue(coordinatorLayout, child));
        } else if (dy < 0 && mScrollTrigger != DIRECTION_DOWN) {
            mScrollTrigger = DIRECTION_DOWN;
            restartAnimator(child, 0f);
        }
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
        if (target instanceof RelativeLayout) {
            return parent.getHeight() - target.getTop();
        }
        return 0f;
    }
}
