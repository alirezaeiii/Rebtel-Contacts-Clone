package com.sample.android.contact.behavior;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * Simple scrolling behavior that monitors nested events in the scrolling
 * container to implement a quick hide/show for the attached view.
 */
public abstract class QuickHideBehavior extends CoordinatorLayout.Behavior<View> {

    protected static final int DIRECTION_UP = 1;
    protected static final int DIRECTION_DOWN = -1;

    /* Tracking last threshold crossed */
    protected int mScrollTrigger;

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
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild,
                                       @NonNull View target, int nestedScrollAxes, int type) {
        //We have to declare interest in the scroll to receive further events
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    /* Helper Methods */

    //Helper to trigger hide/show animation
    protected void restartAnimator(View target, float value) {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
        mAnimator = ObjectAnimator
                .ofFloat(target, View.TRANSLATION_Y, value)
                .setDuration(250);
        mAnimator.start();
    }
}
