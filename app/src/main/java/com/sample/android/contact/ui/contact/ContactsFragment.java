package com.sample.android.contact.ui.contact;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sample.android.contact.Application;
import com.sample.android.contact.R;
import com.sample.android.contact.databinding.FragmentContactsBinding;
import com.sample.android.contact.domain.ContactItem;
import com.sample.android.contact.ui.adapter.ContactsAdapter;
import com.sample.android.contact.util.Resource;
import com.sample.android.contact.viewmodels.ContactsViewModel;
import com.sample.android.contact.widget.HeaderItemDecoration;

import java.util.List;

import javax.inject.Inject;

public class ContactsFragment extends Fragment {

    @Inject
    ContactsViewModel.Factory mFactory;

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

        ContactsAdapter contactsAdapter = new ContactsAdapter(requireActivity().getSupportFragmentManager(), () -> {
            if (binding.searchView.hasFocus()) {
                binding.searchView.clearFocus();
            }
        });
        binding.recyclerView.setAdapter(contactsAdapter);
        binding.recyclerView.addItemDecoration(new HeaderItemDecoration(contactsAdapter));

        binding.swipeRefresh.setColorSchemeResources(R.color.color1);
        binding.swipeRefresh.setOnRefreshListener(viewModel::refresh);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // Check contacts in case of system initiated process death
                if (viewModel.getContacts().getValue() == null && !query.isEmpty()) {
                    binding.searchView.setQuery("", false);
                } else if (!query.isEmpty()) {
                    binding.searchBack.setVisibility(View.VISIBLE);
                    binding.swipeRefresh.setRefreshing(false);
                    binding.swipeRefresh.setEnabled(false);
                    viewModel.clearJob();
                    viewModel.search(query);
                }
                return true;
            }
        });

        int closeBtnId = getResources().getIdentifier("search_close_btn", "id", "android");
        ImageView closeButton = binding.searchView.findViewById(closeBtnId);
        if (closeButton != null) {
            closeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.color3), PorterDuff.Mode.SRC_IN);
        }

        binding.searchBack.setOnClickListener(view -> {
            contactsAdapter.setItems(viewModel.getContacts().getValue(), true);
            binding.searchBack.setVisibility(View.INVISIBLE);
            binding.swipeRefresh.setEnabled(true);
            binding.searchView.setQuery("", false);
        });

        // Create the observer which updates the UI.
        final Observer<Resource<List<ContactItem>>> contactsResourceObserver = resource -> {
            if (resource instanceof Resource.Loading) {
                if (((Resource.Loading) resource).isRefreshing()) {
                    binding.swipeRefresh.setRefreshing(true);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.swipeRefresh.setRefreshing(false);
                }
            }
        };
        final Observer<List<ContactItem>> contactsObserver = contacts -> {
            contactsAdapter.setItems(contacts, true);
            binding.progressBar.setVisibility(View.GONE);
            binding.swipeRefresh.setRefreshing(false);
        };
        final Observer<List<ContactItem>> searchedContactsObserver = searchedContacts ->
                contactsAdapter.setItems(searchedContacts, false);

        // Observe the LiveData, passing in this fragment as the LifecycleOwner and the observer.
        viewModel.getLiveData().observe(this, contactsResourceObserver);
        viewModel.getContacts().observe(this, contactsObserver);
        viewModel.getSearchedContacts().observe(this, searchedContactsObserver);

        return binding.getRoot();
    }
}
