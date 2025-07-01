package com.sample.android.contact.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.sample.android.contact.util.cleanString

@Parcelize
class ContactPhoneNumber @JvmOverloads constructor(
    val number: String,
    val typeLabel: String,
    var flagResId: Int? = null,
    var startPadding: Int? = null,
    var startMargin: Int? = null
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other !is ContactPhoneNumber) {
            return false
        }
        return other.number.cleanString() == number.cleanString()
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
