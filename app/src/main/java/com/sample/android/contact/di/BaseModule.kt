package com.sample.android.contact.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.util.schedulars.BaseSchedulerProvider
import com.sample.android.contact.util.schedulars.SchedulerProvider
import com.sample.android.contact.viewmodels.ContactsViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class BaseModule {

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider

    //expose Application as an injectable context
    @Binds
    internal abstract fun bindContext(application: Application): Context

    @Binds
    internal abstract fun bindViewModelFactory(factory: ContactsViewModel.Factory): ViewModelProvider.Factory
}