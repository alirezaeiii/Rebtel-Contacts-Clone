package com.sample.android.contact.ui.viewholder

import android.view.View
import androidx.fragment.app.FragmentManager

class SearchContactViewHolder(root: View, fragmentManager: FragmentManager) :
    BaseContactViewHolder(root, fragmentManager) {

    override fun isBottomLineVisible() = true
}