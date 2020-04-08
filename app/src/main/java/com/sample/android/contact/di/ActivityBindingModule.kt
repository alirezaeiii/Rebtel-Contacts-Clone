package com.sample.android.contact.di

import com.sample.android.contact.ui.MainActivity
import com.sample.android.contact.ui.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
            modules = [MainModule::class]
    )
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector(
            modules = [SplashModule::class]
    )
    internal abstract fun splashActivity(): SplashActivity
}