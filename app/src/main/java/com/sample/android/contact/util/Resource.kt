package com.sample.android.contact.util

sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    class Reloading<out T> : Resource<T>()
    data class Success<out T>(val data: T?) : Resource<T>()
}