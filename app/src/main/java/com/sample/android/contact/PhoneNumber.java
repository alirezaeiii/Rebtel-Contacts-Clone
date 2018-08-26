package com.sample.android.contact;

public class PhoneNumber {

    private String number;
    private int type;

    public PhoneNumber(String number, int type) {
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
        if(!(o instanceof PhoneNumber)) {
            return false;
        }
        PhoneNumber other = (PhoneNumber) o;
        return other.number.equals(this.number);
    }
}
