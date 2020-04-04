package com.sample.android.contact

import com.sample.android.contact.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ContactApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}