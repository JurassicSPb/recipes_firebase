package com.jurassicspb.recipes_firebase.base

import android.arch.lifecycle.LiveData

class BaseLiveData<State : BaseState> (state: State) : LiveData<State>() {

    init {
        value = state
    }

    override fun getValue() =
        super.getValue() ?: throw IllegalStateException("Value cannot be null. Value should be initialized in init block")

    fun notifyObservers() {
        value = value
    }
}