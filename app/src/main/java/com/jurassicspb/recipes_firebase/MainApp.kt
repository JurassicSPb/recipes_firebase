package com.jurassicspb.recipes_firebase

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.jurassicspb.recipes_firebase.constants.DATABASE_NAME
import com.jurassicspb.recipes_firebase.login.LoginViewModel
import com.jurassicspb.recipes_firebase.recipes.RecipeViewModel
import com.jurassicspb.recipes_firebase.recipes.RecipesActivity
import com.jurassicspb.recipes_firebase.recipes.RecipesAdapter
import com.jurassicspb.recipes_firebase.repository.Repository
import com.jurassicspb.recipes_firebase.storage.AppDataBase
import com.jurassicspb.recipes_firebase.storage.StorageLayer
import com.jurassicspb.recipes_firebase.storage.dao.mappers.RecipeItemToStorageRecipeMapper
import com.jurassicspb.recipes_firebase.storage.dao.mappers.StorageRecipeToRecipeItemMapper
import com.jurassicspb.recipes_firebase.util.AnimationHelper
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

class MainApp : Application() {

    private val module = module {
        single { createRoom(applicationContext) }
        single {
            StorageLayer(
                get(),
                RecipeItemToStorageRecipeMapper(),
                StorageRecipeToRecipeItemMapper()
            )
        }
        single { Repository(get(), get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { RecipeViewModel(get()) }
        single { FirebaseAuth.getInstance() }
        factory { AnimationHelper() }
        factory { RecipesAdapter(get(), applicationContext) }
        factory { RecipesActivity() }
    }

    private fun createRoom(context: Context): AppDataBase =
        Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME).build()

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(module))
    }
}