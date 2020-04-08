package com.sample.android.contact.di

import androidx.lifecycle.ViewModelProvider
import com.sample.android.contact.viewmodels.SplashViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class SplashModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: SplashViewModel.Factory): ViewModelProvider.Factory
}