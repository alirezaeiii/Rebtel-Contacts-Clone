package com.sample.android.contact.model;

public class ContactPhoneNumber {

    public String number;
    public String typeLabel;
    public int flagResId;

    public ContactPhoneNumber(String number, String typeLabel, int flagResId) {
        this.number = number;
        this.typeLabel = typeLabel;
        this.flagResId = flagResId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ContactPhoneNumber)) {
            return false;
        }
        ContactPhoneNumber other = (ContactPhoneNumber) o;
        return other.number.replaceAll("\\s", "").equals(
                this.number.replaceAll("\\s", ""));
    }
}
