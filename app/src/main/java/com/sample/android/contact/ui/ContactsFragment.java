package com.sample.android.contact.ui;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.sample.android.contact.R;
import com.sample.android.contact.model.Contact;
import com.sylversky.indexablelistview.widget.IndexableRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sample.android.contact.util.Utils.getContacts;

public class ContactsFragment extends Fragment {

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.LABEL
    };
    private ContactsAdapter mAdapter;

    @BindView(R.id.recyclerView)
    IndexableRecyclerView mRecyclerView;

    @BindView(R.id.search_view)
    SearchView mSearchView;

    @BindView(R.id.search_back)
    ImageButton mSearchBack;

    @BindView(R.id.appBarLayout)
    View mAppBarLayout;

    @BindView(R.id.progressBar)
    AppCompatImageView mProgressBar;

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, root);

        mAdapter = new ContactsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        // hint, inputType & ime options seem to be ignored from XML! Set in code
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setupAdapter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.isEmpty()) {
                    mSearchBack.setVisibility(View.VISIBLE);
                    setupAdapter(query);
                }
                return true;
            }
        });

        mSearchBack.setOnClickListener(view -> {
            showContacts();
            mSearchBack.setVisibility(View.INVISIBLE);
            mSearchView.setQuery("", false);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        showContacts();
    }

    private void setupAdapter(String query) {
        final String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE ? OR " +
                ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?";
        final String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%"};
        new SetupAdapterAsync(selection, selectionArgs, false).execute();
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            new SetupAdapterAsync(null, null, true).execute();
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
                mAppBarLayout.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Until you grant the permission, we canot display the names", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class SetupAdapterAsync extends AsyncTask<Void, Void, List<Contact>> {

        private String selection;
        private String[] selectionArgs;
        private boolean showSeparator;

        private SetupAdapterAsync(String selection, String[] selectionArgs, boolean showSeparator) {
            this.selection = selection;
            this.selectionArgs = selectionArgs;
            this.showSeparator = showSeparator;
        }

        @Override
        protected void onPreExecute() {
            if(showSeparator) {
                mProgressBar.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected List<Contact> doInBackground(Void...params) {

            final Cursor cursor = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PROJECTION,
                    selection,
                    selectionArgs,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE UNICODE ASC"
            );

            List<Contact> contacts = getContacts(cursor);
            cursor.close();

            return contacts;
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            mAdapter.setItems(contacts, showSeparator);
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
