package com.sample.android.contact.model;

public class CountryCodeNumber {

    public String number;
    public int flagResId;

    public CountryCodeNumber(String number, int flagResId) {
        this.number = number;
        this.flagResId = flagResId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CountryCodeNumber)) {
            return false;
        }
        CountryCodeNumber other = (CountryCodeNumber) o;
        return other.number.replaceAll("\\s", "").equals(
                this.number.replaceAll("\\s", ""));
    }
}
