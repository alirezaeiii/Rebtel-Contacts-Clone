package com.sample.android.contact.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.sample.android.contact.domain.ContactItem
import com.sample.android.contact.repository.ContactsRepository
import com.sample.android.contact.util.Resource
import com.sample.android.contact.util.cleanQueryString
import java.util.regex.Pattern
import javax.inject.Inject

class ContactsViewModel(private val repository: ContactsRepository) : BaseViewModel(repository) {

    val contacts: LiveData<List<ContactItem>?> = liveData.map { resource ->
        if (resource is Resource.Success) resource.data else emptyList()
    }

    private val localSearchedContacts: MutableList<ContactItem> = mutableListOf()

    private val _searchedContacts = MutableLiveData<List<ContactItem>>()
    val searchedContacts: LiveData<List<ContactItem>>
        get() = _searchedContacts

    init {
        // Reload contacts in case of system initiated process death
        if (liveData.value == null) {
            repository.loadContacts()
        }
    }

    fun refresh() {
        repository.refreshContacts()
    }

    fun clearJob() {
        repository.clearDisposable()
    }

    fun search(query: String) {
        localSearchedContacts.clear()
        contacts.value?.forEach { contactItem ->
            val contact = contactItem.contact
            contact?.let {
                if (Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE)
                        .matcher(it.name).find()
                ) {
                    localSearchedContacts.add(contactItem)
                }
                val cleanQuery = query.cleanQueryString()
                if (cleanQuery.matches("^[+\\d]+$".toRegex())) {
                    for (phoneNumber in it.phoneNumbers) {
                        if (Pattern.compile(Pattern.quote(cleanQuery)).matcher(
                                phoneNumber.number.cleanQueryString()
                            ).find() && !localSearchedContacts.contains(contactItem)
                        ) {
                            localSearchedContacts.add(contactItem)
                        }
                    }
                }
            }
        }
        _searchedContacts.value = localSearchedContacts
    }

    /**
     * Factory for constructing ContactsViewModel with parameter
     */
    class Factory @Inject constructor(
        private val repository: ContactsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ContactsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}