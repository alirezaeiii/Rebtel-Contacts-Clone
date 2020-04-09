package com.sample.android.contact.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.ContactsRepository
import com.sample.android.contact.domain.Contact
import com.sample.android.contact.util.Resource
import javax.inject.Inject

class ContactsViewModel(
        private val repository: ContactsRepository)
    : BaseViewModel() {

    private val _liveData = MutableLiveData<Resource<List<Contact>>>()
    val liveData: LiveData<Resource<List<Contact>>>
        get() = _liveData

    init {
        _liveData.value = Resource.Loading()
        setContacts()
    }

    fun showContacts(selection: String?, selectionArgs: Array<String>?) {
        repository.queryDb(selection, selectionArgs)
        setContacts()
    }

    private fun setContacts() {
        repository.contacts.subscribe {
            _liveData.postValue(Resource.Success(it))
        }.also { compositeDisposable.add(it) }
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
            private val repository: ContactsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ContactsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}