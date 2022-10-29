package com.sample.android.contact

import com.sample.android.contact.di.ApplicationComponent
import com.sample.android.contact.di.DaggerApplicationComponent

class Application : android.app.Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .bindContext(this)
            .build()
    }
}