package com.sample.android.contact

import android.app.IntentService
import android.content.Intent
import android.os.Parcelable
import com.sample.android.contact.ui.ContactsFragment.CONTACTS
import com.sample.android.contact.util.ContactUtil


class ContactsService : IntentService("ContactsService") {

    override fun onHandleIntent(intent: Intent?) {
        val cursor = ContactUtil.getContactsCursor(null, null, this)
        val contacts = ContactUtil.getContacts(cursor, this)
        cursor?.close()
        val contactsIntent = Intent(CONTACTS_RECEIVER)
        contactsIntent.putParcelableArrayListExtra(CONTACTS, ArrayList<Parcelable>(contacts))
        sendBroadcast(contactsIntent)
    }
}

const val CONTACTS_RECEIVER = "com.sample.android.contact.receiver"