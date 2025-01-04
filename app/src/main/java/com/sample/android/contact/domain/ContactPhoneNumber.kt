package com.sample.android.contact.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
        return normalizeNumber(other.number) == normalizeNumber(number)
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    private fun normalizeNumber(number: String): String = number.replace("\\s".toRegex(), "")
}
