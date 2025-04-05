package com.sample.android.contact.ui.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sample.android.contact.domain.ContactItem;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    protected ContactItem contactItem;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected abstract void bind();

    public void bind(ContactItem contactItem) {
        this.contactItem = contactItem;
        bind();
    }
}