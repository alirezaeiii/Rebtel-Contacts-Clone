package com.sample.android.contact

import android.content.Context
import android.provider.ContactsContract
import com.sample.android.contact.domain.Contact
import com.sample.android.contact.util.ContactUtil
import com.sample.android.contact.util.ContactUtil.PROJECTION
import com.sample.android.contact.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepository
@Inject constructor() // Requires empty public constructor
{
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var schedulerProvider: BaseSchedulerProvider

    lateinit var contacts: Observable<List<Contact>>

    fun queryDb(selection: String?, selectionArgs: Array<String>?) {
        val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION,
                selection,
                selectionArgs,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE UNICODE ASC")
        contacts = Observable.create(ObservableOnSubscribe<List<Contact>>
        { emitter -> emitter.onNext(ContactUtil.getContacts(cursor, context)) })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doFinally { cursor?.close() }
    }
}