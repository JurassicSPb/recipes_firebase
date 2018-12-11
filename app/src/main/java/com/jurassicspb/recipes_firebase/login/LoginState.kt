package com.jurassicspb.recipes_firebase.login

import com.jurassicspb.recipes_firebase.base.BaseState

class LoginState : BaseState() {
    var showStartAnimation = SingleEvent(false)
    var userExists = SingleEvent(false)
    val message = Message()
    val authResult = AuthResult()
    var showProgress = SingleEvent(false)

    inner class Message {
        val showWrongEmail = SingleEvent(false)
        val showWrongPassword = SingleEvent(false)
    }

    inner class AuthResult {
        val showError : SingleEvent<String> = SingleEvent("")
        val isSuccessful = SingleEvent(false)
    }
}