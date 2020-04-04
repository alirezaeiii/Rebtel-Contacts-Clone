package com.sample.android.contact.di

import com.sample.android.contact.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
            modules = [MainModule::class]
    )
    internal abstract fun mainActivity(): MainActivity
}