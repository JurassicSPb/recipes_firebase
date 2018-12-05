package com.jurassicspb.recipes_firebase.login

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import com.jurassicspb.recipes_firebase.extensions.execute
import com.jurassicspb.recipes_firebase.extensions.isValidEmail
import com.jurassicspb.recipes_firebase.repository.Repository
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(private val repository: Repository) : ViewModel(), LifecycleObserver {
    var data = LoginLiveData(LoginState())
        private set
    private var restoring = false
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun onSignInButtonClicked(email: String, password: String) {
        if (!isInputValid(email, password)) return

        repository.signIn(email, password).execute(compositeDisposable)
    }

    fun onRegisterButtonClicked(email: String, password: String) {
        if (!isInputValid(email, password)) return

        repository.register(email, password).execute(compositeDisposable)
    }

    fun onOwnerCreated(restoring: Boolean) {
        if (restoring) {
            this.restoring = restoring
            data.value.showStartAnimation.value = false
            data.notifyObservers()
            return
        }

        repository.getAuthResult().execute(
            compositeDisposable,
            onNext = { handleAuthResult(it) })
    }

    private fun handleAuthResult(it: Repository.AuthResult) {
        if (it.message != null || it.message != ""){
            data.value.authResult.showError.value = it.message!!
            data.notifyObservers()
        }
        if (it.isSuccessful) {
            data.value.authResult.isSuccessful.value = true
            data.notifyObservers()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        repository.getAuthUser().execute(
            compositeDisposable,
            onSuccess = {
                if (it) {
                    data.value.userExists.value = true
                    data.notifyObservers()
                }
            })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (!restoring) {
            data.value.showStartAnimation.value = true
            data.notifyObservers()
        }
    }

    private fun isInputValid(email: String, password: String): Boolean {
        if (!email.isValidEmail()) {
            data.value.message.showWrongEmail.value = true
            data.notifyObservers()
            return false
        }
        if (password.length < MIN_PASSWORD_LENGTH) {
            data.value.message.showWrongPassword.value = true
            data.notifyObservers()
            return false
        }
        return true
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }
}