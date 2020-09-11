package com.sample.android.contact.di

import android.app.Application
import android.content.Context
import com.sample.android.contact.util.schedulars.BaseSchedulerProvider
import com.sample.android.contact.util.schedulars.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider

    //expose Application as an injectable context
    @Binds
    internal abstract fun bindContext(application: Application): Context
}