package com.sample.android.contact.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ContactItem @JvmOverloads constructor(
        var contact: Contact? = null,
        var contactSeparator: ContactSeparator? = null
) : Parcelable

@Parcelize
class Contact @JvmOverloads constructor(
        val name: String,
        val phoneNumbers: Set<ContactPhoneNumber>? = null,
        val briefName: String? = null,
        val accentName: String? = null,
        val flagResIds: Set<Int>? = null,
        // State of the item
        var isExpanded: Boolean = false,
        var showBottomLine: Boolean = true,
        var showChildBottomLine: Boolean = true
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
}

@Parcelize
class ContactSeparator @JvmOverloads constructor(
        var showSeparator: Boolean,
        var separatorChar: Char? = null
) : Parcelable

@Parcelize
class ContactPhoneNumber @JvmOverloads constructor(
        var number: String,
        val typeLabel: String,
        var flagResId: Int? = null,
        var startPadding: Int? = null,
        var startMargin: Int? = null
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other !is ContactPhoneNumber) {
            return false
        }
        return other.number.replace("\\s".toRegex(), "") ==
                number.replace("\\s".toRegex(), "")
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}