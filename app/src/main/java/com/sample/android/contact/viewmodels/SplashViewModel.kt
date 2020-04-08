package com.sample.android.contact.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.ContactsRepository
import javax.inject.Inject

class SplashViewModel(
        repository: ContactsRepository)
    : BaseViewModel() {

    init {
        repository.getContacts(null, null)
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
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}