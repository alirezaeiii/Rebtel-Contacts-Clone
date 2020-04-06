package com.sample.android.contact.viewmodels

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.model.Contact
import com.sample.android.contact.ui.ContactsFragment
import com.sample.android.contact.util.ContactUtil
import com.sample.android.contact.util.Resource
import com.sample.android.contact.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable
import javax.inject.Inject

class ContactsViewModel(
        schedulerProvider: BaseSchedulerProvider,
        app: Application)
    : BaseViewModel(schedulerProvider, app) {

    private val _liveData = MutableLiveData<Resource<List<Contact>>>()
    val liveData: LiveData<Resource<List<Contact>>>
        get() = _liveData

    private val context = app

    init {
        _liveData.value = Resource.Loading()
    }

    fun showContacts(selection: String?, selectionArgs: Array<String>?) {
        val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                ContactsFragment.PROJECTION,
                selection,
                selectionArgs,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE UNICODE ASC")
        composeObservable {
            Observable.just(ContactUtil.getContacts(cursor, context))
        }.doFinally {
            cursor?.close()
        }.subscribe {
            _liveData.postValue(Resource.Success(it))
        }.also {
            compositeDisposable.add(it)
        }
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
            private val schedulerProvider: BaseSchedulerProvider,
            private val app: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ContactsViewModel(schedulerProvider, app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}