package com.sample.android.contact.ui.viewholder;

import android.content.Context;
import android.view.View;

import com.sample.android.contact.databinding.ContactItemBinding;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactPhoneNumber;
import com.sample.android.contact.util.ContactUtils;

public abstract class BaseContactViewHolder extends BaseViewHolder {

    private final ContactItemBinding binding;

    private final Context context;

    public BaseContactViewHolder(View root) {
        super(root);
        binding = ContactItemBinding.bind(root);
        context = root.getContext();
    }

    protected abstract boolean isBottomLineVisible();

    @Override
    public void bind() {
        Contact contact = contactItem.getContact();
        binding.flagItem.setImageResource(contact.getFlagResIds().iterator().next());
        binding.bottomLine.setVisibility(isBottomLineVisible() ? View.VISIBLE : View.GONE);
        ContactPhoneNumber phoneNumber = contact.getPhoneNumbers().iterator().next();
        binding.phoneType.setText(phoneNumber.getTypeLabel());
        binding.contactName.setText(contact.getName());
        binding.imageText.setText(contact.getBriefName());
        binding.phoneNumber.setText(phoneNumber.getNumber());
        binding.contactLayout.setOnClickListener(view -> ContactUtils.call(context, phoneNumber.getNumber()));
    }
}