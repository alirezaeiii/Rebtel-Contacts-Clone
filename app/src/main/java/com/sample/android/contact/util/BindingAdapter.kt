package com.sample.android.contact.util

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("showLoading")
fun ProgressBar.showLoading(resource: Resource<*>?) {
    visibility = if (resource is Resource.Loading) View.VISIBLE else View.GONE
}


@BindingAdapter("showData")
fun View.showData(resource: Resource<*>?) {
    visibility = if (resource is Resource.Success || resource is Resource.Reloading) View.VISIBLE else View.GONE
}

@BindingAdapter("app:refreshing")
fun SwipeRefreshLayout.refreshing(resource: Resource<*>?) {
    isRefreshing = resource is Resource.Reloading
}