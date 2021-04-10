package com.sample.android.contact.util.schedulars

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides different types of schedulers.
 */
@Singleton
class SchedulerProvider @Inject constructor() : BaseSchedulerProvider {

    override fun computation(): Scheduler = Schedulers.computation()

    override fun io(): Scheduler = Schedulers.io()
}