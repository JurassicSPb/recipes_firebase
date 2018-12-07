package com.jurassicspb.recipes_firebase.storage.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.jurassicspb.recipes_firebase.storage.entities.StorageRecipe
import io.reactivex.Maybe

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipes: List<StorageRecipe>)

    @Query("SELECT * FROM recipes")
    fun getAll(): Maybe<List<StorageRecipe>>
}