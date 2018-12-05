package com.jurassicspb.recipes_firebase.login

import android.arch.lifecycle.LiveData

class LoginLiveData (state: LoginState) : LiveData<LoginState>() {

    init {
        value = state
    }

    override fun getValue() =
        super.getValue() ?: throw IllegalStateException("Value cannot be null. Value should be initialized in init block")

    fun notifyObservers() {
        value = value
    }
}