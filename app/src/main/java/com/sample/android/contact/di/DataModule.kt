package com.sample.android.contact.di

import com.sample.android.contact.ui.adapter.ContactsAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideContactsAdapter(): ContactsAdapter = ContactsAdapter()
}