package com.sample.android.contact.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

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
    protected void directionUpScrolling() {
    }

    @Override
    protected void directionDownScrolling() {
    }

    @Override
    protected float getTargetHideValue(ViewGroup parent, View target) {
        return -target.getHeight();
    }
}
