package com.sample.android.contact.data

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.android.contact.domain.Contact
import com.sample.android.contact.util.ContactUtil
import com.sample.android.contact.util.ContactUtil.PROJECTION
import com.sample.android.contact.util.Resource
import com.sample.android.contact.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsDataSource @Inject constructor(
        private val context: Context,
        private val schedulerProvider: BaseSchedulerProvider) {

    private val _liveData = MutableLiveData<Resource<List<Contact>>>()
    val liveData: LiveData<Resource<List<Contact>>>
        get() = _liveData

    fun loadAllContacts() : Disposable {
        _liveData.value = Resource.Loading()
        return loadContacts(null, null)
    }

    fun loadContacts(selection: String?, selectionArgs: Array<String>?) : Disposable {
        val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                selection,
                selectionArgs,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE UNICODE ASC")
        return Observable.create(ObservableOnSubscribe<List<Contact>>
        { emitter -> emitter.onNext(ContactUtil.getContacts(cursor, context)) })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnComplete {
                    cursor?.close()
                }
                .subscribe {
                    _liveData.postValue(Resource.Success(it))
                }
    }
}