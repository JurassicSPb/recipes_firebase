package com.jurassicspb.recipes_firebase.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.koin.android.viewmodel.ext.android.viewModelByClass
import kotlin.reflect.KClass

abstract class BaseActivity<State : BaseState, ViewModel : BaseViewModel<State>>(vmClass: KClass<ViewModel>): AppCompatActivity() {

    abstract val layoutRes: Int

    val viewModel: ViewModel by viewModelByClass(vmClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutRes)

        lifecycle.addObserver(viewModel)

        viewModel.onOwnerCreated(savedInstanceState != null)

        viewModel.data.observe(this, Observer<State> { it ->
            it?.let {
                applyChanges(it)
            }
        })
    }

    override fun onDestroy() {
        lifecycle.removeObserver(viewModel)

        super.onDestroy()
    }

    abstract fun applyChanges(it: State)
}