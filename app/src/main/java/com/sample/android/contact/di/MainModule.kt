package com.sample.android.contact.di

import com.sample.android.contact.ui.contact.ContactsFragment
import com.sample.android.contact.ui.contact.DialpadFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    internal abstract fun contactsFragment(): ContactsFragment

    @ContributesAndroidInjector
    internal abstract fun dialpadFragment(): DialpadFragment
}