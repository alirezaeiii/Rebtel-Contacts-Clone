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
    var type: Type = Type.SINGLE
) : Parcelable {

    enum class Type {
        SINGLE,
        MULTIPLE
    }
}