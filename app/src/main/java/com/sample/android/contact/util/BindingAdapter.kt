package com.sample.android.contact.util

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("showLoading")
fun <T> ProgressBar.showLoading(resource: Resource<T>?) {
    visibility = if (resource is Resource.Loading) View.VISIBLE else View.GONE
}


@BindingAdapter("showData")
fun <T> View.showData(resource: Resource<T>?) {
    visibility = if (resource is Resource.Success) View.VISIBLE else View.GONE
}