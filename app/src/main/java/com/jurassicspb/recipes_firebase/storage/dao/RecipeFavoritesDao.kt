package com.jurassicspb.recipes_firebase.storage.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.jurassicspb.recipes_firebase.storage.entities.StorageFavorite
import io.reactivex.Maybe

@Dao
interface RecipeFavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorites: List<StorageFavorite>)

    @Query("SELECT favId FROM favorites")
    fun getAll(): Maybe<List<Long>>

    @Query("DELETE FROM favorites")
    fun deleteAll()

}