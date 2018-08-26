package com.sample.android.contact;

import java.util.List;

public class Contact {

    private String name;
    private List<PhoneNumber> numbers;

    public Contact(String name, List<PhoneNumber> numbers) {
        this.name = name;
        this.numbers = numbers;
    }

    public String getName() {
        return name;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return numbers;
    }

    public void setNumbers(List<PhoneNumber> numbers) {
        this.numbers = numbers;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) o;
        return other.name.equals(this.name);
    }
}
