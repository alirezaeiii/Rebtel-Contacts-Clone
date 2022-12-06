package com.sample.android.contact.ui.viewholder

import android.view.View

class SearchContactMultipleViewHolder(root: View, clickListener: ClickListener) :
    BaseContactMultipleViewHolder(root, clickListener) {

    override fun getBottomLineVisibility() = View.VISIBLE

    override fun getShowChildBottomLine() = true
}