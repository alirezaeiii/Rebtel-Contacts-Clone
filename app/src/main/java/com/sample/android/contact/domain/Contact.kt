package com.sample.android.contact.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Contact @JvmOverloads constructor(
    val name: String,
    val phoneNumbers: Set<ContactPhoneNumber>? = null,
    val briefName: String? = null,
    val flagResIds: Set<Int>? = null,
    // State of the item
    var isExpanded: Boolean = false,
    var showBottomLine: Boolean = true,
    var showChildBottomLine: Boolean = true,
    var contactType: ContactType = ContactType.SINGLE
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other !is Contact) {
            return false
        }
        return other.name == name
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    enum class ContactType {
        SINGLE,
        MULTIPLE
    }
}