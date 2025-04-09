package com.sample.android.contact.ui.contact;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sample.android.contact.Application;
import com.sample.android.contact.R;
import com.sample.android.contact.databinding.FragmentContactsBinding;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.domain.ContactItem;
import com.sample.android.contact.domain.ContactPhoneNumber;
import com.sample.android.contact.ui.adapter.ContactsAdapter;
import com.sample.android.contact.util.Resource;
import com.sample.android.contact.viewmodels.ContactsViewModel;
import com.sample.android.contact.widget.HeaderItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class ContactsFragment extends Fragment {

    @Inject
    ContactsViewModel.Factory mFactory;

    private List<ContactItem> mContacts;

    private ContactsAdapter mAdapter;

    private final List<ContactItem> mSearchedContacts = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Application) context.getApplicationContext()).getApplicationComponent()
                .inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentContactsBinding binding = FragmentContactsBinding.inflate(inflater, container, false);
        ContactsViewModel viewModel = new ViewModelProvider(this, mFactory).get(ContactsViewModel.class);

        mAdapter = new ContactsAdapter(requireActivity().getSupportFragmentManager(), () -> {
            if (binding.searchView.hasFocus()) {
                binding.searchView.clearFocus();
            }
        });
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.addItemDecoration(new HeaderItemDecoration(mAdapter));

        binding.swipeRefresh.setColorSchemeResources(R.color.color1);
        binding.swipeRefresh.setOnRefreshListener(viewModel::refresh);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // Check contacts in case of system initiated process death
                if (mContacts == null) {
                    binding.searchView.post(() -> binding.searchView.setQuery("", false));
                } else if (!query.isEmpty()) {
                    binding.searchBack.setVisibility(View.VISIBLE);
                    binding.swipeRefresh.setRefreshing(false);
                    binding.swipeRefresh.setEnabled(false);
                    viewModel.clearJob();
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
            if (resource instanceof Resource.Loading) {
                if (((Resource.Loading) resource).isRefreshing()) {
                    binding.swipeRefresh.setRefreshing(true);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.swipeRefresh.setRefreshing(false);
                }
            } else {
                mContacts = ((Resource.Success<List<ContactItem>>) resource).getData();
                mAdapter.setItems(mContacts, true);
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
            }
        };
        // Observe the LiveData, passing in this fragment as the LifecycleOwner and the observer.
        viewModel.getLiveData().observe(this, contactsObserver);

        return binding.getRoot();
    }

    private void search(String query) {
        mSearchedContacts.clear();
        for (ContactItem contactItem : mContacts) {
            Contact contact = contactItem.getContact();
            if (contact != null) {
                if (Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE)
                        .matcher(contact.getName()).find()) {
                    mSearchedContacts.add(contactItem);
                }
                query = getClean(query);
                if (query.matches("^[+\\d]+$")) {
                    for (ContactPhoneNumber phoneNumber : contact.getPhoneNumbers()) {
                        if (Pattern.compile(Pattern.quote(query)).matcher(
                                getClean(phoneNumber.getNumber())).find() &&
                                !mSearchedContacts.contains(contactItem)) {
                            mSearchedContacts.add(contactItem);

                        }
                    }
                }
            }
        }
        mAdapter.setItems(mSearchedContacts, false);
    }

    private String getClean(String res) {
        return res.replaceAll("\\s+", "").replaceAll("\\.", "");
    }
}
