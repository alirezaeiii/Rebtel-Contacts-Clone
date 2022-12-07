package com.sample.android.contact.ui.viewholder

import android.view.View

class SearchContactMultipleViewHolder(root: View, clickListener: ClickListener) :
    BaseContactMultipleViewHolder(root, clickListener) {

    override fun isBottomLineVisible() = true

    override fun isChildBottomLineVisible() = true
}