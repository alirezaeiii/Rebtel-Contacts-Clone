package com.sample.android.contact.util

sealed class Resource<out T> {
    class Loading(val isRefreshing: Boolean) : Resource<Nothing>()
    data class Success<out T>(val data: T?) : Resource<T>()
}