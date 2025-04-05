package com.sample.android.contact.ui.viewholder;

import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.sample.android.contact.databinding.ContactItemBinding;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactPhoneNumber;
import com.sample.android.contact.ui.contact.CallDialogFragment;

public abstract class BaseContactViewHolder extends BaseViewHolder {

    private final ContactItemBinding binding;

    private final FragmentManager fragmentManager;

    public BaseContactViewHolder(View root, FragmentManager fragmentManager) {
        super(root);
        binding = ContactItemBinding.bind(root);
        this.fragmentManager = fragmentManager;
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
        binding.contactLayout.setOnClickListener(view -> {
            CallDialogFragment bottomSheet = new CallDialogFragment(contact.getName(), phoneNumber.getNumber(), contact.getFlagResIds().iterator().next());
            bottomSheet.show(fragmentManager, CALL_FRAGMENT_DIALOG_TAG);

        });
    }
}