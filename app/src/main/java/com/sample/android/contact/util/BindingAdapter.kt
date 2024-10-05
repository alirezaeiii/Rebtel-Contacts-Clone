package com.sample.android.contact.util

import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("showData")
fun <T> View.showData(resource: Resource<T>?) {
    visibility = if (resource is Resource.Success) View.VISIBLE else View.GONE
}