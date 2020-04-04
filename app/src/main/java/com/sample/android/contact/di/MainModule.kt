package com.sample.android.contact.di

import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.ui.ContactsFragment
import com.sample.android.contact.ui.DialpadFragment
import com.sample.android.contact.viewmodels.ContactsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    internal abstract fun contactsFragment(): ContactsFragment

    @ContributesAndroidInjector
    internal abstract fun dialpadFragment(): DialpadFragment

    @Binds
    internal abstract fun bindViewModelFactory(factory: ContactsViewModel.Factory): ViewModelProvider.Factory
}