package com.sample.android.contact.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ContactItem @JvmOverloads constructor(
    var contact: Contact? = null,
    var contactSeparator: String? = null
) : Parcelable

fun ContactItem.isNotSeparator() = contactSeparator == null
