package com.sample.android.contact.model;

import java.util.List;

public class Contact {

    private String name;
    private List<ContactPhoneNumber> numbers;
    private String briefName;
    // State of the item
    private boolean expanded;

    public Contact(String name, List<ContactPhoneNumber> numbers, String briefName) {
        this.name = name;
        this.numbers = numbers;
        this.briefName = briefName;
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

    public String getBriefName() {
        return briefName;
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
