package com.sample.android.contact.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.android.contact.R;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactPhoneNumber;

import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseContactViewHolder extends BaseViewHolder {

    @BindView(R.id.contact_name)
    TextView contactNameView;

    @BindView(R.id.phone_number)
    TextView phoneNumberView;

    @BindView(R.id.phone_type)
    TextView phoneNumberType;

    @BindView(R.id.flagItem)
    ImageView flagImageView;

    @BindView(R.id.image_text)
    TextView imageText;

    @BindView(R.id.bottomLine)
    View bottomLine;

    public BaseContactViewHolder(View root) {
        super(root);
        ButterKnife.bind(this, root);
    }

    protected abstract int getBottomLineVisibility();

    @Override
    public void bind() {
        Contact contact = contactItem.getContact();
        Iterator<Integer> flags = contact.getFlagResIds().iterator();
        flagImageView.setImageResource(flags.next());
        bottomLine.setVisibility(getBottomLineVisibility());
        Set<ContactPhoneNumber> numbers = contact.getPhoneNumbers();
        Iterator<ContactPhoneNumber> iterator = numbers.iterator();
        ContactPhoneNumber phoneNumber = iterator.next();
        phoneNumberType.setText(phoneNumber.getTypeLabel());
        contactNameView.setText(contact.getName());
        imageText.setText(contact.getBriefName());
        phoneNumberView.setText(phoneNumber.getNumber());
    }
}
