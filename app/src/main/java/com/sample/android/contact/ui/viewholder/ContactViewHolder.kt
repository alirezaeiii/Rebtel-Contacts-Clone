package com.sample.android.contact.ui.viewholder

import android.view.View

class ContactViewHolder(root: View) : BaseContactViewHolder(root) {

    override fun isBottomLineVisible() = contactItem.contact!!.showBottomLine
}