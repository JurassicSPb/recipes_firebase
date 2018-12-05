package com.jurassicspb.recipes_firebase.login

import android.content.Intent
import android.os.Bundle
import com.jurassicspb.recipes_firebase.R
import com.jurassicspb.recipes_firebase.base.BaseActivity
import com.jurassicspb.recipes_firebase.extensions.toast
import com.jurassicspb.recipes_firebase.recipes.RecipesActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.email_login_form as form
import kotlinx.android.synthetic.main.activity_login.email_register_button as register
import kotlinx.android.synthetic.main.activity_login.email_sign_in_button as signIn

class LoginActivity : BaseActivity<LoginState, LoginViewModel>(LoginViewModel::class) {
    override val layoutRes: Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signIn.setOnClickListener {
            viewModel.onSignInButtonClicked(email.text.toString(), password.text.toString())
        }
        register.setOnClickListener {
            viewModel.onRegisterButtonClicked(email.text.toString(), password.text.toString())
        }
    }

    override fun applyChanges(it: LoginState) {
        showStartAnimation(it.showStartAnimation)
        showMessage(it.message)
        checkUserExists(it.userExists)
        checkAuthResult(it.authResult)
    }

    private fun checkAuthResult(authResult: LoginState.AuthResult) = with(authResult) {
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

    private fun startActivity() {
        startActivity(Intent(this, RecipesActivity::class.java))
    }
}