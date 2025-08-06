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
import androidx.lifecycle.ViewModelProvider;

import com.sample.android.contact.Application;
import com.sample.android.contact.R;
import com.sample.android.contact.databinding.FragmentContactsBinding;
import com.sample.android.contact.ui.adapter.ContactsAdapter;
import com.sample.android.contact.viewmodels.ContactsViewModel;
import com.sample.android.contact.widget.HeaderItemDecoration;

import javax.inject.Inject;

public class ContactsFragment extends Fragment {

    @Inject
    ContactsViewModel.Factory mFactory;

    private FragmentContactsBinding mBinding;
    private ContactsViewModel mViewModel;
    private ContactsAdapter mContactsAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Application) context.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentContactsBinding.inflate(inflater, container, false);
        initViewModel();
        setupRecyclerView();
        setupSearchView();
        setupObservers();
        setupListeners();
        return mBinding.getRoot();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(requireActivity(), mFactory).get(ContactsViewModel.class);
    }

    private void setupRecyclerView() {
        mContactsAdapter = new ContactsAdapter(requireActivity().getSupportFragmentManager(), () -> {
            if (mBinding.searchView.hasFocus()) {
                mBinding.searchView.clearFocus();
            }
        });
        mBinding.recyclerView.setAdapter(mContactsAdapter);
        mBinding.recyclerView.addItemDecoration(new HeaderItemDecoration(mContactsAdapter));
    }

    private void setupSearchView() {
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // Check contacts in case of system initiated process death
                if (mViewModel.getContacts().getValue() == null && !query.isEmpty()) {
                    mBinding.searchView.setQuery("", false);
                } else if (!query.isEmpty()) {
                    mViewModel.clearJob();
                    mViewModel.search(query);
                }
                return true;
            }
        });

        int closeBtnId = getResources().getIdentifier("search_close_btn", "id", "android");
        ImageView closeButton = mBinding.searchView.findViewById(closeBtnId);
        if (closeButton != null) {
            closeButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.color3), PorterDuff.Mode.SRC_IN);
        }
    }

    private void setupObservers() {
        mViewModel.getRefreshing().observe(getViewLifecycleOwner(), isRefreshing -> {
            if (isRefreshing != null) {
                mBinding.swipeRefresh.setRefreshing(isRefreshing);
                mBinding.progressBar.setVisibility(isRefreshing ? View.GONE : View.VISIBLE);
            }
        });

        mViewModel.getContacts().observe(getViewLifecycleOwner(), contacts -> {
            if (contacts != null && mViewModel.getSearchedContacts().getValue() == null) {
                mContactsAdapter.setItems(contacts, true);
                stopLoadingUI();
            }
        });

        mViewModel.getSearchedContacts().observe(getViewLifecycleOwner(), searchedContacts -> {
            if (searchedContacts != null) {
                mContactsAdapter.setItems(searchedContacts, false);
                mBinding.searchBack.setVisibility(View.VISIBLE);
                mBinding.swipeRefresh.setEnabled(false);
                stopLoadingUI();
            }
        });
    }

    private void setupListeners() {
        mBinding.swipeRefresh.setColorSchemeResources(R.color.color1);
        mBinding.swipeRefresh.setOnRefreshListener(mViewModel::refresh);

        mBinding.searchBack.setOnClickListener(view -> {
            mContactsAdapter.setItems(mViewModel.getContacts().getValue(), true);
            mBinding.searchBack.setVisibility(View.INVISIBLE);
            mBinding.swipeRefresh.setEnabled(true);
            mBinding.searchView.setQuery("", false);
            mViewModel.resetSearch();
        });
    }

    private void stopLoadingUI() {
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.swipeRefresh.setRefreshing(false);
    }
}
