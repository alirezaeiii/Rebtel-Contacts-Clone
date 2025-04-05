package com.sample.android.contact.ui.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager

class SearchContactMultipleViewHolder(
    root: View,
    fragmentManager: FragmentManager,
    clickListener: ClickListener
) : BaseContactMultipleViewHolder(root, fragmentManager, clickListener) {

    override fun isBottomLineVisible() = true

    override fun isChildBottomLineVisible() = true
}