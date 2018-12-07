package com.jurassicspb.recipes_firebase.storage

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.jurassicspb.recipes_firebase.storage.dao.RecipeDao
import com.jurassicspb.recipes_firebase.storage.dao.RecipeDeletedDao
import com.jurassicspb.recipes_firebase.storage.dao.RecipeFavoritesDao
import com.jurassicspb.recipes_firebase.storage.entities.StorageRecipe

@Database(entities = [StorageRecipe::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

//    abstract fun recipeFavoritesDao(): RecipeFavoritesDao

//    abstract fun recipeDeletedDao(): RecipeDeletedDao

}