package com.sample.android.contact.ui.viewholder;

import android.view.View;

import androidx.annotation.NonNull;

import com.sample.android.contact.databinding.ContactSeparatorBinding;

public class SeparatorViewHolder extends BaseViewHolder {

    private final ContactSeparatorBinding binding;

    public SeparatorViewHolder(@NonNull View root) {
        super(root);
        binding = ContactSeparatorBinding.bind(root);
    }

    @Override
    public void bind() {
        binding.separatorText.setText(contactItem.getContactSeparator());
    }
}