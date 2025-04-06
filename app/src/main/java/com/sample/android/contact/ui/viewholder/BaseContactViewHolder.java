package com.sample.android.contact.ui.viewholder;

import static com.sample.android.contact.util.ContactUtils.openCallDialog;

import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.sample.android.contact.databinding.ContactItemBinding;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactPhoneNumber;
import com.sample.android.contact.ui.adapter.ContactsAdapter;

public abstract class BaseContactViewHolder extends BaseViewHolder {

    private final ContactItemBinding binding;

    private final FragmentManager fragmentManager;

    private final ContactsAdapter.OnItemClickListener clickListener;

    public BaseContactViewHolder(View root, FragmentManager fragmentManager, ContactsAdapter.OnItemClickListener clickListener) {
        super(root);
        binding = ContactItemBinding.bind(root);
        this.fragmentManager = fragmentManager;
        this.clickListener = clickListener;
    }

    protected abstract boolean isBottomLineVisible();

    @Override
    public void bind() {
        Contact contact = contactItem.getContact();
        int flagResId = contact.getFlagResIds().iterator().next();
        binding.flagItem.setImageResource(flagResId);
        binding.bottomLine.setVisibility(isBottomLineVisible() ? View.VISIBLE : View.GONE);
        ContactPhoneNumber phoneNumber = contact.getPhoneNumbers().iterator().next();
        binding.phoneType.setText(phoneNumber.getTypeLabel());
        binding.contactName.setText(contact.getName());
        binding.imageText.setText(contact.getBriefName());
        binding.phoneNumber.setText(phoneNumber.getNumber());
        binding.contactLayout.setOnClickListener(view -> openCallDialog(contact.getName(),
                phoneNumber.getNumber(), flagResId, fragmentManager,
                clickListener));
    }
}