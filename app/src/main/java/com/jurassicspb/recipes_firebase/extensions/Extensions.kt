package com.jurassicspb.recipes_firebase.extensions

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.Property
import android.view.View
import android.widget.Toast
import com.jurassicspb.recipes_firebase.R
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


fun CharSequence.isValidEmail() = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Context.toast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, stringRes, duration).show()
}

fun Context.toast(string: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, string, duration).show()
}

fun Context.drawable(@DrawableRes drawableRes: Int): Drawable? = ContextCompat.getDrawable(this, drawableRes)

fun Context.drawableOrDefault(@DrawableRes drawableRes: Int, @DrawableRes default: Int = R.drawable.item_decorator) =
    this.drawable(drawableRes) ?: this.drawable(default) ?: throw IllegalArgumentException("Default drawable value is null")

fun Context.color(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Context.dimenPx(@DimenRes dimenRes: Int): Int = resources.getDimensionPixelSize(dimenRes)

fun Context.dimenPxFloat(@DimenRes dimenRes: Int): Float = resources.getDimension(dimenRes)

fun <T>MutableList<T>.swap(from: Int, to: Int){
    val tmp = this[from]
    this[from] = this[to]
    this[to] = tmp
}

fun View.toAnimator(
    propertyName: Property<View, Float>,
    propertyValue: Float,
    duration: Long = -1L,
    interpolator: TimeInterpolator? = null
): ObjectAnimator {
    val animator = ObjectAnimator.ofFloat(this, propertyName, propertyValue)
    if (duration > 0L) animator.duration = duration
    interpolator?.let { animator.interpolator = it }
    return animator
}

fun <E> Observable<E>.execute(
    compositeDisposable: CompositeDisposable,
    onNext: (E) -> Unit = emptySuccess(),
    onSubscribe: (Disposable) -> Unit = emptySubscribe(),
    onComplete: () -> Unit = emptyComplete(),
    onFinished: () -> Unit = emptyFinished(),
    onError: (Throwable) -> Unit = emptyError()
) {
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(object : Observer<E> {
            override fun onComplete() {
                onComplete()
                onFinished()
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                onSubscribe(d)
            }

            override fun onNext(t: E) {
                onNext(t)
            }

            override fun onError(e: Throwable) {
                onError(e)
                onFinished()
            }
        })
}

fun Completable.execute(
    compositeDisposable: CompositeDisposable,
    onSubscribe: (Disposable) -> Unit = emptySubscribe(),
    onComplete: () -> Unit = emptyComplete(),
    onFinished: () -> Unit = emptyFinished(),
    onError: (Throwable) -> Unit = emptyError()
) {
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(object : CompletableObserver {
            override fun onComplete() {
                onComplete()
                onFinished()
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                onSubscribe(d)
            }

            override fun onError(e: Throwable) {
                onError(e)
                onFinished()
            }
        })
}

fun <E> Maybe<E>.execute(
    compositeDisposable: CompositeDisposable,
    onSuccess: (E) -> Unit = emptySuccess(),
    onSubscribe: (Disposable) -> Unit = emptySubscribe(),
    onComplete: () -> Unit = emptyComplete(),
    onFinished: () -> Unit = emptyFinished(),
    onError: (Throwable) -> Unit = emptyError()
) {
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(object : MaybeObserver<E> {
            override fun onSuccess(t: E) {
                onSuccess(t)
            }

            override fun onComplete() {
                onComplete()
                onFinished()
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                onSubscribe(d)
            }

            override fun onError(e: Throwable) {
                onError(e)
                onFinished()
            }
        })
}

fun <E> emptySuccess(): (E) -> Unit = {}

fun emptySubscribe(): (Disposable) -> Unit = {}

fun emptyError(): (Throwable) -> Unit = {}

fun emptyFinished(): () -> Unit = {}

fun emptyComplete(): () -> Unit = {}
