package com.sample.android.contact;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        showContacts();
    }

    public void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.

            String[] projection = new String[]{
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.TYPE
            };

            final Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE UNICODE ASC"
            );

            List<Contact> contacts = new ArrayList<>();

            int nameIndex = cursor.getColumnIndex(projection[0]);
            int numberIndex = cursor.getColumnIndex(projection[1]);
            int typeIndex = cursor.getColumnIndex(projection[2]);

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


            ContactsAdapter adapter = new ContactsAdapter(this, contacts);

            // Create the list view and bind the adapter
            ExpandableListView listView = (ExpandableListView) findViewById(R.id.listview);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_LONG).show();
            }
        }
    }
}
