package com.sample.android.contact.ui.viewholder

import android.view.View

class ContactViewHolder(root: View) : BaseContactViewHolder(root) {

    override fun getBottomLineVisibility() =
        if (contactItem.contact!!.showBottomLine) View.VISIBLE else View.GONE
}