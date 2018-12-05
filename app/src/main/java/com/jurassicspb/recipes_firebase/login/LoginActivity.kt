package com.jurassicspb.recipes_firebase.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jurassicspb.recipes_firebase.R
import com.jurassicspb.recipes_firebase.extensions.toast
import com.jurassicspb.recipes_firebase.recipes.RecipesActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.activity_login.email_login_form as form
import kotlinx.android.synthetic.main.activity_login.email_sign_in_button as signIn
import kotlinx.android.synthetic.main.activity_login.email_register_button as register

class LoginActivity : AppCompatActivity() {
    private val mainViewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        lifecycle.addObserver(mainViewModel)

        mainViewModel.onOwnerCreated(savedInstanceState != null)

        mainViewModel.data.observe(this, Observer { it ->
            it?.let {
                showStartAnimation(it.showStartAnimation)
                showMessage(it.message)
                checkUserExists(it.userExists)
                checkAuthResult(it.authResult)
            }
        })

        signIn.setOnClickListener {
            mainViewModel.onSignInButtonClicked(email.text.toString(), password.text.toString())
        }
        register.setOnClickListener {
            mainViewModel.onRegisterButtonClicked(email.text.toString(), password.text.toString())
        }
    }

    private fun checkAuthResult(authResult: LoginState.AuthResult) = with(authResult){
        if (showError.needToShow) toast(showError.value)
        if (isSuccessful.needToShow && isSuccessful.value) startActivity()
    }

    private fun checkUserExists(userExists: SingleEvent<Boolean>) {
        if (userExists.needToShow && userExists.value) startActivity()
    }

    private fun showMessage(message: LoginState.Message) = with(message) {
        if (showWrongEmail.needToShow && showWrongEmail.value) toast(R.string.wrong_email)
        if (showWrongPassword.needToShow && showWrongPassword.value) toast(R.string.wrong_password)
    }

    private fun showStartAnimation(showStartAnimation: SingleEvent<Boolean>) {
        if (showStartAnimation.needToShow && showStartAnimation.value) {
            motion_layout.loadLayoutDescription(R.xml.login_scene)
            motion_layout.transitionToEnd()
        }
    }

    private fun startActivity(){
        startActivity(Intent(this, RecipesActivity::class.java))
    }
}