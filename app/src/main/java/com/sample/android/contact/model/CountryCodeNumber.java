package com.sample.android.contact.model;

public class CountryCodeNumber {

    public String number;
    public String regionCode;

    public CountryCodeNumber(String number, String regionCode) {
        this.number = number;
        this.regionCode = regionCode;
    }

    public CountryCodeNumber(String number) {
        this.number = number;
    }
}
