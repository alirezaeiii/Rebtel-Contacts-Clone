package com.sample.android.contact.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sample.android.contact.BR;
import com.sample.android.contact.R;
import com.sample.android.contact.databinding.FragmentContactsBinding;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.util.Resource;
import com.sample.android.contact.viewmodels.ContactsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ContactsFragment extends DaggerFragment {

    private static final String TAG = ContactsFragment.class.getSimpleName();

    @Inject
    ContactsViewModel.Factory mFactory;

    private ContactsAdapter mAdapter;

    private List<Contact> mContacts;

    private List<Contact> mTempContacts;

    @Inject
    public ContactsFragment() {
        // Requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        ContactsViewModel viewModel = new ViewModelProvider(this, mFactory).get(ContactsViewModel.class);
        FragmentContactsBinding binding = FragmentContactsBinding.bind(root);
        binding.setVariable(BR.vm, viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        mAdapter = new ContactsAdapter(mContacts);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(mAdapter);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        binding.searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        // hint, inputType & ime options seem to be ignored from XML! Set in code
        binding.searchView.setQueryHint(getString(R.string.search_hint));
        binding.searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.isEmpty()) {
                    binding.searchBack.setVisibility(View.VISIBLE);
                    search(query);
                }
                return true;
            }
        });
        int searchCloseIconButtonId = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchClose = binding.searchView.findViewById(searchCloseIconButtonId);
        int searchCloseIconColor = ResourcesCompat.getColor(getResources(), R.color.color3, null);
        searchClose.setColorFilter(searchCloseIconColor);

        binding.searchBack.setOnClickListener(view -> {
            mAdapter.setItems(mContacts, true);
            binding.searchBack.setVisibility(View.INVISIBLE);
            binding.searchView.setQuery("", false);
        });

        // Create the observer which updates the UI.
        final Observer<Resource<List<Contact>>> contactsObserver = resource -> {
            if (resource instanceof Resource.Success) {
                mContacts = ((Resource.Success<List<Contact>>) resource).getData();
                mAdapter.setItems(mContacts, true);
            }
        };
        // Observe the LiveData, passing in this fragment as the LifecycleOwner and the observer.
        viewModel.getLiveData().observe(this, contactsObserver);

        return root;
    }

    private void search(String query) {
        if (mContacts == null) {
            return;
        }
        if (mTempContacts == null) {
            mTempContacts = new ArrayList<>();
        } else {
            mTempContacts.clear();
        }
        query = query.toLowerCase().trim();
        for (Contact contact : mContacts) {
            if (contact.getName().toLowerCase().trim().contains(query)) {
                mTempContacts.add(contact);
            }
        }
        mAdapter.setItems(mTempContacts, false);
    }
}
