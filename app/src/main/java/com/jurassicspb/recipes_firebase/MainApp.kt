package com.jurassicspb.recipes_firebase

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.jurassicspb.recipes_firebase.login.LoginViewModel
import com.jurassicspb.recipes_firebase.recipes.RecipesActivity
import com.jurassicspb.recipes_firebase.recipes.RecipesAdapter
import com.jurassicspb.recipes_firebase.repository.Repository
import com.jurassicspb.recipes_firebase.util.AnimationHelper
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

class MainApp : Application() {

    private val module = module {
        single { Repository(get()) }
        viewModel { LoginViewModel(get()) }
        single { FirebaseAuth.getInstance() }
        factory { AnimationHelper() }
        factory { RecipesAdapter(get(), applicationContext) }
        factory { RecipesActivity() }

    }

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(module))
    }
}