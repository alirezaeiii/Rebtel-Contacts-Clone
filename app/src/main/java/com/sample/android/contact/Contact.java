package com.sample.android.contact;

import java.util.List;

public class Contact {

    private String name;
    private List<ContactPhoneNumber> numbers;
    // State of the item
    private boolean expanded;

    public Contact(String name, List<ContactPhoneNumber> numbers) {
        this.name = name;
        this.numbers = numbers;
    }

    public String getName() {
        return name;
    }

    public List<ContactPhoneNumber> getPhoneNumbers() {
        return numbers;
    }

    public void setNumbers(List<ContactPhoneNumber> numbers) {
        this.numbers = numbers;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
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
