package com.sample.android.contact.di

import android.app.Application
import com.sample.android.contact.ContactApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [ActivityBindingModule::class,
            AndroidSupportInjectionModule::class,
            BaseModule::class]
)
interface AppComponent : AndroidInjector<ContactApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}