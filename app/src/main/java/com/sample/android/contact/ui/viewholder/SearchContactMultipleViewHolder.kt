package com.sample.android.contact.ui.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import com.sample.android.contact.ui.adapter.ContactsAdapter.OnItemClickListener

class SearchContactMultipleViewHolder(
    root: View,
    fragmentManager: FragmentManager,
    clickListener: ClickListener,
    onItemClickListener: OnItemClickListener
) : BaseContactMultipleViewHolder(root, fragmentManager, clickListener, onItemClickListener) {

    override fun isBottomLineVisible() = true

    override fun isChildBottomLineVisible() = true
}