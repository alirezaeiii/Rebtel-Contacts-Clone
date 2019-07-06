package com.sample.android.contact.model;

import android.widget.ImageView;

import java.util.List;

public class Contact {

    private String name;
    private List<ContactPhoneNumber> numbers;
    private String briefName;
    private List<ImageView> imageViews;
    // State of the item
    private boolean expanded;

    public Contact(String name, List<ContactPhoneNumber> numbers, String briefName,
                   List<ImageView> imageViews) {
        this.name = name;
        this.numbers = numbers;
        this.briefName = briefName;
        this.imageViews = imageViews;
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

    public void setImageViews(List<ImageView> imageViews) {
        this.imageViews = imageViews;
    }

    public List<ImageView> getImageViews() {
        return imageViews;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) o;
        return other.name.equals(this.name);
    }
}
