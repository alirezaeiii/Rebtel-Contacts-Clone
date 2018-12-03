package com.sample.android.contact;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.disposables.Disposable;

import static com.sample.android.contact.ContactsActivity.PROJECTION;

class Utils {

    static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    static String getTypeValue(int type) {
        String typeValue;
        switch (type) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                typeValue = "Home";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                typeValue = "Mobile";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                typeValue = "Work";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                typeValue = "Work Fax";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                typeValue = "Home Fax";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                typeValue = "Pager";
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                typeValue = "Main";
                break;
            default:
                typeValue = "Other";
                break;
        }
        return typeValue;
    }

    static void unsubscribe(Disposable subscription) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        } // else subscription doesn't exist or already unsubscribed
    }

    static List<Contact> getContacts(Cursor cursor) {
        List<Contact> contacts = new ArrayList<>();

        int nameIndex = cursor.getColumnIndex(PROJECTION[0]);
        int numberIndex = cursor.getColumnIndex(PROJECTION[1]);
        int typeIndex = cursor.getColumnIndex(PROJECTION[2]);

        while (cursor.moveToNext()) {

            String name = cursor.getString(nameIndex);
            String number = cursor.getString(numberIndex);
            int type = cursor.getInt(typeIndex);

            List<PhoneNumber> numbers = new ArrayList<>();
            PhoneNumber phoneNumber = new PhoneNumber(
                    PhoneNumberUtils.normalizeNumber(number),
                    type);
            numbers.add(phoneNumber);
            Contact contact = new Contact(name, numbers);
            int index = contacts.indexOf(contact);

            if (index == -1) {
                contacts.add(contact);
            } else {
                contact = contacts.get(index);
                numbers = contact.getPhoneNumbers();
                if (numbers.indexOf(phoneNumber) == -1) {
                    numbers.add(phoneNumber);
                    contact.setNumbers(numbers);
                    contacts.set(index, contact);
                }
            }
        }
        return contacts;
    }
}
