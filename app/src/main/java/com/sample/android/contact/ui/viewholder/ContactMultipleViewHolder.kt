package com.sample.android.contact.ui.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager

class ContactMultipleViewHolder(
    root: View,
    fragmentManager: FragmentManager,
    clickListener: ClickListener
) : BaseContactMultipleViewHolder(root, fragmentManager, clickListener) {

    override fun isBottomLineVisible() = contactItem.contact!!.showBottomLine

    override fun isChildBottomLineVisible() = contactItem.contact!!.showChildBottomLine
}