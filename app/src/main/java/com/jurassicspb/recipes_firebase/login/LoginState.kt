package com.jurassicspb.recipes_firebase.login

class LoginState {
    var showStartAnimation = SingleEvent(false)
    var userExists = SingleEvent(false)
    val message = Message()
    val authResult = AuthResult()

    inner class Message {
        val showWrongEmail = SingleEvent(false)
        val showWrongPassword = SingleEvent(false)
    }

    inner class AuthResult {
        val showError : SingleEvent<String> = SingleEvent("")
        val isSuccessful = SingleEvent(false)
    }
}