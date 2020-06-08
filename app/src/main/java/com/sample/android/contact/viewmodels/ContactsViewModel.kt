package com.sample.android.contact.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.data.ContactsDataSource
import com.sample.android.contact.domain.Contact
import com.sample.android.contact.util.Resource
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ContactsViewModel(private val dataSource: ContactsDataSource) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _liveData = dataSource.liveData
    val liveData: LiveData<Resource<List<Contact>>>
        get() = _liveData

    fun loadAllContacts() {
        dataSource.loadAllContacts().also { compositeDisposable.add(it) }
    }

    fun loadContacts(selection: String?, selectionArgs: Array<String>?) {
        dataSource.loadContacts(selection, selectionArgs).also { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    /**
     * Factory for constructing ContactsViewModel with parameter
     */
    class Factory @Inject constructor(
            private val dataSource: ContactsDataSource
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ContactsViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}