package com.sample.android.contact.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.domain.Contact
import com.sample.android.contact.repository.ContactsRepository
import com.sample.android.contact.util.Resource
import javax.inject.Inject

class SplashViewModel(private val repository: ContactsRepository) : ViewModel() {

    private val _liveData = repository.liveData
    val liveData: LiveData<Resource<List<Contact>>>
        get() = _liveData

    fun loadContacts() {
        repository.loadContacts()
    }

    /**
     * Factory for constructing SplashViewModel with parameter
     */
    class Factory @Inject constructor(
            private val repository: ContactsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}