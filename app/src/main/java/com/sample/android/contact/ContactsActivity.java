package com.sample.android.contact;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.reactivex.disposables.Disposable;

import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.sylversky.indexablelistview.widget.IndexableRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.sample.android.contact.Utils.unsubscribe;

public class ContactsActivity extends AppCompatActivity {

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ContactsAdapter mAdapter;

    private Disposable mSearchViewTextSubscription;

    private final static String[] projection = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        IndexableRecyclerView recyclerView = findViewById(R.id.recyclerView);
        mAdapter = new ContactsAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        showContacts();
    }

    public void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.

            final Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE UNICODE ASC"
            );

            mAdapter.setItems(getContacts(cursor), true);
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                showContacts();
                return true;
            }
        });

        mSearchViewTextSubscription = RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (charSequence.length() > 0) {

                        final String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ? OR " +
                                ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?";
                        final String[] selectionArgs = new String[]{"%" + charSequence + "%", "%" + charSequence + "%"};
                        final Cursor cursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                projection,
                                selection,
                                selectionArgs,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE UNICODE ASC"
                        );

                        runOnUiThread(() -> {
                            mAdapter.setItems(getContacts(cursor), false);
                            cursor.close();
                        });
                    }
                });

        return true;
    }

    @Override
    protected void onDestroy() {
        unsubscribe(mSearchViewTextSubscription);
        super.onDestroy();
    }

    private List<Contact> getContacts(Cursor cursor) {
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
        return contacts;
    }
}
