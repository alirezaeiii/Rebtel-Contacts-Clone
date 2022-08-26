package com.sample.android.contact.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sample.android.contact.R;
import com.sample.android.contact.domain.ContactItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeparatorViewHolder extends BaseViewHolder {

    @BindView(R.id.separator)
    View separatorView;

    @BindView(R.id.separator_text)
    TextView separatorText;

    public SeparatorViewHolder(@NonNull View root) {
        super(root);
        ButterKnife.bind(this, root);
    }

    @Override
    public void bind(ContactItem contactItem, boolean showSeparator) {
        if (showSeparator) {
            separatorText.setText(String.valueOf(contactItem.getContactSeparator()));
            separatorView.setVisibility(View.VISIBLE);
        } else {
            separatorView.setVisibility(View.GONE);
        }
    }
}
