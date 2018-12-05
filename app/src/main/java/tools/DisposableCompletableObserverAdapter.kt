package tools

import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

abstract class DisposableCompletableObserverAdapter : CompletableObserver {
    override fun onSubscribe(d: Disposable) { // empty stub for Completable method
    }

    override fun onComplete() { // empty stub for Completable method
    }

    override fun onError(e: Throwable) { // empty stub for Completable method
    }
}