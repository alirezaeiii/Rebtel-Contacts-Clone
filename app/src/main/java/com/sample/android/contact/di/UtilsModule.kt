package com.sample.android.contact.di

import com.sample.android.contact.util.schedulars.BaseSchedulerProvider
import com.sample.android.contact.util.schedulars.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class UtilsModule {

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider
}