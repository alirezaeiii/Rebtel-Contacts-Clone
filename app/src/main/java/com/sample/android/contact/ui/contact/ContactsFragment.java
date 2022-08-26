package com.sample.android.contact.ui.contact;

import android.os.Bundle;
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
import com.sample.android.contact.domain.ContactItem;
import com.sample.android.contact.ui.adapter.ContactsAdapter;
import com.sample.android.contact.ui.adapter.HeaderItemDecoration;
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

    private List<ContactItem> mContacts;

    private List<ContactItem> mSearchedContacts;

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

        mAdapter = new ContactsAdapter();
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.addItemDecoration(new HeaderItemDecoration(mAdapter));

        binding.swipeRefresh.setColorSchemeResources(R.color.color1);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.refresh();
            binding.swipeRefresh.setRefreshing(false);
        });

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
                    binding.swipeRefresh.setEnabled(false);
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
            binding.swipeRefresh.setEnabled(true);
            binding.searchView.setQuery("", false);
        });

        // Create the observer which updates the UI.
        final Observer<Resource<List<ContactItem>>> contactsObserver = resource -> {
            if (resource instanceof Resource.Success) {
                mContacts = ((Resource.Success<List<ContactItem>>) resource).getData();
                mAdapter.setItems(mContacts, true);
            }
        };
        // Observe the LiveData, passing in this fragment as the LifecycleOwner and the observer.
        viewModel.getLiveData().observe(this, contactsObserver);

        this.mSearchedContacts = new ArrayList<>();

        return root;
    }

    private void search(String query) {
        mSearchedContacts.clear();
        query = query.toLowerCase().trim();
        for (ContactItem contactItem : mContacts) {
            Contact contact = contactItem.getContact();
            if (contact != null && contact.getName().toLowerCase().trim().contains(query)) {
                mSearchedContacts.add(contactItem);
            }
        }
        mAdapter.setItems(mSearchedContacts, false);
    }
}
