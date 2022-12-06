package com.sample.android.contact.ui.viewholder

import android.view.View

class ContactMultipleViewHolder(root: View, clickListener: ClickListener) :
    BaseContactMultipleViewHolder(root, clickListener) {

    override fun getBottomLineVisibility() =
        if (contactItem.contact!!.showBottomLine) View.VISIBLE else View.GONE

    override fun getShowChildBottomLine() = contactItem.contact!!.showChildBottomLine
}