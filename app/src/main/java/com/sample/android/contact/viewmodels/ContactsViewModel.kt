package com.sample.android.contact.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.repository.ContactsRepository
import javax.inject.Inject

class ContactsViewModel(private val repository: ContactsRepository) : BaseViewModel(repository) {

    init {
        // Reload contacts in case of system initiated process death
        if (liveData.value == null) {
            repository.loadContacts()
        }
    }

    fun refresh() {
        repository.refreshContacts()
    }

    fun clear() {
        repository.clearDisposable()
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
