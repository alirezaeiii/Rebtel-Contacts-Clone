package com.sample.android.contact.di

import com.sample.android.contact.ui.ContactsFragment
import com.sample.android.contact.ui.DialpadFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    internal abstract fun contactsFragment(): ContactsFragment

    @ContributesAndroidInjector
    internal abstract fun dialpadFragment(): DialpadFragment
}