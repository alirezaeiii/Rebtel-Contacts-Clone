package com.sample.android.contact.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.android.contact.BR;
import com.sample.android.contact.R;
import com.sample.android.contact.databinding.FragmentContactsBinding;
import com.sample.android.contact.domain.Contact;
import com.sample.android.contact.util.Resource;
import com.sample.android.contact.viewmodels.ContactsViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

public class ContactsFragment extends DaggerFragment {

    @Inject
    ContactsViewModel.Factory mFactory;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_view)
    SearchView mSearchView;

    @BindView(R.id.search_back)
    ImageButton mSearchBack;

    private Unbinder unbinder;

    private ContactsAdapter mAdapter;

    private List<Contact> mContacts;

    private List<Contact> mTempContact;

    @Inject
    public ContactsFragment() {
        // Requires empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, root);

        ContactsViewModel viewModel = new ViewModelProvider(this, mFactory).get(ContactsViewModel.class);
        ViewDataBinding binding = FragmentContactsBinding.bind(root);
        binding.setVariable(BR.vm, viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

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
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.isEmpty()) {
                    mSearchBack.setVisibility(View.VISIBLE);
                    search(query);
                }
                return true;
            }
        });

        int searchCloseIconButtonId = getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchClose = mSearchView.findViewById(searchCloseIconButtonId);
        int searchCloseIconColor = ResourcesCompat.getColor(getResources(), R.color.color3, null);
        searchClose.setColorFilter(searchCloseIconColor);

        mSearchBack.setOnClickListener(view -> {
            mAdapter.setItems(mContacts, true);
            mSearchBack.setVisibility(View.INVISIBLE);
            mSearchView.setQuery("", false);
        });

        // Create the observer which updates the UI.
        final Observer<Resource<List<Contact>>> contactsObserver = resource -> {
            if (resource instanceof Resource.Success) {
                List<Contact> items = ((Resource.Success<List<Contact>>) resource).getData();
                mContacts = items;
                mAdapter.setItems(items, true);
            }
        };

        // Observe the LiveData, passing in this fragment as the LifecycleOwner and the observer.
        viewModel.getLiveData().observe(this, contactsObserver);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void search(String query) {
        if (mTempContact == null) {
            mTempContact = new ArrayList<>();
        } else {
            mTempContact.clear();
        }
        int textLength = query.length();
        for (Contact contact : mContacts) {
            if (textLength <= contact.getName().length() &&
                    contact.getName().toLowerCase().contains(query.toLowerCase())) {
                mTempContact.add(contact);
            }
        }
        mAdapter.setItems(mTempContact, false);
    }
}
