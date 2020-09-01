package com.sample.android.contact.viewmodels

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.sample.android.contact.domain.Contact
import com.sample.android.contact.repository.ContactsRepository
import com.sample.android.contact.util.Resource
import javax.inject.Inject

class ContactsViewModel(private val state: SavedStateHandle,
                        repository: ContactsRepository) : ViewModel() {

    private val _contacts = MutableLiveData((state.getLiveData<List<Contact>>(LIVE_DATA)))

    private val _liveData = Transformations.switchMap(_contacts) { repository.liveData }
    val liveData: LiveData<Resource<List<Contact>>>
        get() = _liveData

    fun saveState() {
        state.set(LIVE_DATA, (liveData.value as Resource.Success<List<Contact>>).data)
    }

    /**
     * Factory for constructing ContactsViewModel with parameter
     */
    class Factory @Inject constructor(
            private val repository: ContactsRepository,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
            if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ContactsViewModel(handle, repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

private const val LIVE_DATA = "liveData"