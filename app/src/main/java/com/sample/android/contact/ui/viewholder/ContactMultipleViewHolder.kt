package com.sample.android.contact.ui.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import com.sample.android.contact.ui.adapter.ContactsAdapter.OnItemClickListener

class ContactMultipleViewHolder(
    root: View,
    fragmentManager: FragmentManager,
    clickListener: ClickListener,
    onItemClickListener: OnItemClickListener
) : BaseContactMultipleViewHolder(root, fragmentManager, clickListener, onItemClickListener) {

    override fun isBottomLineVisible() = contactItem.contact!!.showBottomLine

    override fun isChildBottomLineVisible() = contactItem.contact!!.showChildBottomLine
}