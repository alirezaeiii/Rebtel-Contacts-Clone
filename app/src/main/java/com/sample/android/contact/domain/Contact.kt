package com.sample.android.contact.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Contact @JvmOverloads constructor(
    val name: String,
    val phoneNumbers: Set<ContactPhoneNumber>,
    val briefName: String,
    val flagResIds: Set<Int>,
    var showBottomLine: Boolean = true,
    var showChildBottomLine: Boolean = true,
    var type: Type = Type.SINGLE,
    var isExpanded: Boolean = false
) : Parcelable {

    enum class Type {
        SINGLE,
        MULTIPLE
    }
}