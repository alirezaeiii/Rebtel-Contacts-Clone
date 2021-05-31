package com.sample.android.contact.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sample.android.contact.R;
import com.sample.android.contact.domain.ContactItem;

import butterknife.BindView;
import butterknife.ButterKnife;

class SeparatorViewHolder extends BaseViewHolder {

    @BindView(R.id.separator)
    View separatorView;

    @BindView(R.id.separator_text)
    TextView separatorText;

    private final boolean showSeparator;

    public SeparatorViewHolder(@NonNull View root, boolean showSeparator) {
        super(root);
        this.showSeparator = showSeparator;
        ButterKnife.bind(this, root);
    }

    @Override
    protected void bind(ContactItem contactItem) {
        if (showSeparator) {
            separatorText.setText(String.valueOf(contactItem.getContactSeparator()));
            separatorView.setVisibility(View.VISIBLE);
        } else {
            separatorView.setVisibility(View.GONE);
        }
    }
}
