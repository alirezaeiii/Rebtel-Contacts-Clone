package com.sample.android.contact.model;

public class ContactPhoneNumber {

    private String number;
    private int type;
    private String typeLabel;

    public ContactPhoneNumber(String number, int type, String typeLabel) {
        this.number = number;
        this.type = type;
        this.typeLabel = typeLabel;
    }

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

    public String getTypeLabel() {
        return typeLabel;
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
