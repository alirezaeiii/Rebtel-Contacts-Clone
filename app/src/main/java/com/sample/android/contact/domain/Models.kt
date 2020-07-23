package com.sample.android.contact.domain

class Contact @JvmOverloads constructor(
        val name: String,
        val phoneNumbers: Set<ContactPhoneNumber>? = null,
        val briefName: String? = null,
        val accentName: String? = null,
        val flagResIds: Set<Int>? = null,
        // State of the item
        var isExpanded: Boolean = false,
        var contactSeparator: ContactSeparator? = null,
        var showLine: Boolean = true,
        var lineFlag: Boolean = true) {

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

class ContactSeparator(
        var showSeparator: Boolean = false,
        var separatorChar: Char)

class ContactPhoneNumber @JvmOverloads constructor(
        var number: String,
        val typeLabel: String,
        var flagResId: Int? = null,
        var lpMargin: Int? = null,
        var rlpMargin: Int? = null) {

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