package com.sample.android.contact.repository

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.android.contact.domain.Contact
import com.sample.android.contact.util.ContactUtils
import com.sample.android.contact.util.ContactUtils.PROJECTION
import com.sample.android.contact.util.Resource
import com.sample.android.contact.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepository @Inject constructor(
        private val context: Context,
        private val schedulerProvider: BaseSchedulerProvider
) {

    private val compositeDisposable = CompositeDisposable()

    private val _liveData = MutableLiveData<Resource<List<Contact>>>()
    val liveData: LiveData<Resource<List<Contact>>>
        get() = _liveData

    fun loadContacts() {
        _liveData.value = Resource.Loading()
        composeObservable {
            context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PROJECTION,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME +
                            " COLLATE UNICODE ASC")
        }.flatMap { cursor ->
            composeObservable { ContactUtils.getContacts(cursor, context) }
                    .doOnComplete { cursor.close() }
        }.subscribeOn(schedulerProvider.io())
                .doFinally { clear() }
                .subscribe { _liveData.postValue(Resource.Success(it)) }
                .also { compositeDisposable.add(it) }
    }

    fun refreshContacts() {
        clear()
        loadContacts()
    }

    fun clear() {
        compositeDisposable.clear()
    }

    private inline fun <T> composeObservable(crossinline task: () -> T): Observable<T> =
            Observable.fromCallable<T> { task() }
}