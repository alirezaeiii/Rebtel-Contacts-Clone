package com.sample.android.contact;

public class ContactPhoneNumber {

    private String number;
    private int type;

    public ContactPhoneNumber(String number, int type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ContactPhoneNumber)) {
            return false;
        }
        ContactPhoneNumber other = (ContactPhoneNumber) o;
        return other.number.equals(this.number);
    }
}
