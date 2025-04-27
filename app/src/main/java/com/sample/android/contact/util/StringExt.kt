package com.sample.android.contact.util

fun String.cleanQueryString(): String = cleanString().replace("\\.".toRegex(), "")

fun String.cleanString(): String = replace("\\s+".toRegex(), "")