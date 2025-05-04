package com.sample.android.contact.ui.contact;

import static com.sample.android.contact.util.ContactUtils.getActionBarHeight;

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
import com.sample.android.contact.viewmodels.ContactsViewModel;
import com.sample.android.contact.widget.HeaderItemDecoration;

import java.util.List;

import javax.inject.Inject;

public class ContactsFragment extends Fragment {

    private static final int SWIPE_REFRESH_OFFSET = 64;

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
        binding.swipeRefresh.setProgressViewOffset(true, 0, getActionBarHeight(requireContext()) + SWIPE_REFRESH_OFFSET);

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
            viewModel.resetSearch();
        });

        // Create the observer which updates the UI.
        final Observer<Boolean> refresingObserver = isRefreshing -> {
            if (isRefreshing != null) {
                binding.swipeRefresh.setRefreshing(isRefreshing);
                binding.progressBar.setVisibility(isRefreshing ? View.GONE : View.VISIBLE);

            }
        };
        final Observer<List<ContactItem>> contactsObserver = contacts -> {
            if (contacts != null && viewModel.getSearchedContacts().getValue() == null) {
                contactsAdapter.setItems(contacts, true);
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
            }
        };
        final Observer<List<ContactItem>> searchedContactsObserver = searchedContacts -> {
            if (searchedContacts != null) {
                contactsAdapter.setItems(searchedContacts, false);
                binding.searchBack.setVisibility(View.VISIBLE);
                binding.swipeRefresh.setRefreshing(false);
                binding.swipeRefresh.setEnabled(false);
                binding.progressBar.setVisibility(View.GONE);
            }
        };

        // Observe the LiveData, passing in this fragment as the LifecycleOwner and the observer.
        viewModel.getRefreshing().observe(this, refresingObserver);
        viewModel.getContacts().observe(this, contactsObserver);
        viewModel.getSearchedContacts().observe(this, searchedContactsObserver);

        return binding.getRoot();
    }
}
