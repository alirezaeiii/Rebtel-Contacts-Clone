package com.sample.android.contact.ui.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager

class ContactViewHolder(root: View, fragmentManager: FragmentManager) :
    BaseContactViewHolder(root, fragmentManager) {

    override fun isBottomLineVisible() = contactItem.contact!!.showBottomLine
}