package com.sample.android.contact.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.android.contact.R;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactItem;
import com.sample.android.contact.domain.ContactPhoneNumber;

import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sample.android.contact.util.ContactUtils.toVisibility;

class ContactViewHolder extends BaseViewHolder {

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

    private final boolean showSeparator;

    public ContactViewHolder(View root, boolean showSeparator) {
        super(root);
        this.showSeparator = showSeparator;
        ButterKnife.bind(this, root);
    }

    @Override
    protected void bind(ContactItem contactItem) {
        Contact contact = contactItem.getContact();
        Iterator<Integer> flags = contact.getFlagResIds().iterator();
        flagImageView.setImageResource(flags.next());
        toVisibility(showSeparator, bottomLine, contact.getShowBottomLine());
        Set<ContactPhoneNumber> numbers = contact.getPhoneNumbers();
        Iterator<ContactPhoneNumber> iterator = numbers.iterator();
        ContactPhoneNumber phoneNumber = iterator.next();
        phoneNumberType.setText(phoneNumber.getTypeLabel());
        contactNameView.setText(contact.getName());
        imageText.setText(contact.getBriefName());
        phoneNumberView.setText(phoneNumber.getNumber());
    }
}
