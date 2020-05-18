package com.sample.android.contact.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.android.contact.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class ChildViewHolder {

    @BindView(R.id.contact_number)
    TextView contactNumber;

    @BindView(R.id.type)
    TextView numberType;

    @BindView(R.id.child_line)
    View childLine;

    @BindView(R.id.child_top_line)
    View childTopLine;

    @BindView(R.id.frameLayout)
    View frameLayout;

    @BindView(R.id.relativeLayout)
    View relativeLayout;

    @BindView(R.id.flagImageView)
    ImageView flagImageView;

    ChildViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
