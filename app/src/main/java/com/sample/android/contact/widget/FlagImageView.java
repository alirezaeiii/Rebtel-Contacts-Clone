package com.sample.android.contact.widget;

import android.content.Context;
import android.widget.ImageView;

public class FlagImageView extends ImageView {

    public FlagImageView(Context context) {
        super(context);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FlagImageView)) {
            return false;
        }
        FlagImageView other = (FlagImageView) o;
        return other.getDrawable().getConstantState().equals(
                this.getDrawable().getConstantState());
    }
}
