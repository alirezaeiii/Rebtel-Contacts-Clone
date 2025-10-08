package com.sample.android.contact.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sample.android.contact.domain.ContactItem
import com.sample.android.contact.repository.ContactsRepository
import com.sample.android.contact.util.Resource

open class BaseViewModel(private val repository: ContactsRepository): ViewModel() {

    private val _liveData = repository.liveData
    val liveData: LiveData<Resource<List<ContactItem>>>
        get() = _liveData

    override fun onCleared() {
        super.onCleared()
        repository.clearDisposable()
    }
}