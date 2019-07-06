package com.sample.android.contact.model;

public class ContactPhoneNumber {

    private CountryCodeNumber number;
    private String typeLabel;

    public ContactPhoneNumber(CountryCodeNumber number, String typeLabel) {
        this.number = number;
        this.typeLabel = typeLabel;
    }

    public CountryCodeNumber getNumber() {
        return number;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ContactPhoneNumber)) {
            return false;
        }
        ContactPhoneNumber other = (ContactPhoneNumber) o;
        return other.number.equals(this.number);
    }
}
