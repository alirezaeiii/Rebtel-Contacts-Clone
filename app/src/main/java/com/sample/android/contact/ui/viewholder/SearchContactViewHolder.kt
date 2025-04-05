package com.sample.android.contact.ui.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager
import com.sample.android.contact.ui.adapter.ContactsAdapter.OnItemClickListener

class SearchContactViewHolder(
    root: View, fragmentManager:
    FragmentManager,
    clickListener: OnItemClickListener
) : BaseContactViewHolder(root, fragmentManager, clickListener) {

    override fun isBottomLineVisible() = true
}