package tools

import io.reactivex.observers.DisposableMaybeObserver

abstract class DisposableMaybeObserverAdapter<T> : DisposableMaybeObserver<T>() {
    override fun onSuccess(t: T) { // empty stub for MaybeObservable method
    }

    override fun onError(e: Throwable) { // empty stub for MaybeObservable method
    }

    override fun onComplete() { // empty stub for MaybeObservable method
    }
}