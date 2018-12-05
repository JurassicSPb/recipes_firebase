package com.jurassicspb.recipes_firebase.base

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<State : BaseState>(state: State) : ViewModel(), LifecycleObserver {
    val compositeDisposable = CompositeDisposable()

    var data = BaseLiveData(state)
        private set

    abstract fun onOwnerCreated(restoring: Boolean)

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}