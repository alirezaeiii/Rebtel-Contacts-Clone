package com.sample.android.contact.di

import android.content.Context
import com.sample.android.contact.ui.contact.ContactsFragment
import com.sample.android.contact.ui.splash.SplashActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UtilsModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindContext(context: Context): Builder

        fun build(): ApplicationComponent
    }

    fun inject(splashActivity: SplashActivity)
    fun inject(contactsFragment: ContactsFragment)
}