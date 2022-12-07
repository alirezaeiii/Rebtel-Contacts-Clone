package com.sample.android.contact.ui.viewholder

import android.view.View

class ContactMultipleViewHolder(root: View, clickListener: ClickListener) :
    BaseContactMultipleViewHolder(root, clickListener) {

    override fun isBottomLineVisible() = contactItem.contact!!.showBottomLine

    override fun isChildBottomLineVisible() = contactItem.contact!!.showChildBottomLine
}