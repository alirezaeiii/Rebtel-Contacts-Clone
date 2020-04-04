package com.sample.android.contact.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sample.android.contact.util.schedulars.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(private val schedulerProvider: BaseSchedulerProvider,
                         app: Application) : AndroidViewModel(app) {

    protected val compositeDisposable = CompositeDisposable()

    protected fun <T> composeObservable(task: () -> Observable<T>): Observable<T> = task()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all disposables;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}