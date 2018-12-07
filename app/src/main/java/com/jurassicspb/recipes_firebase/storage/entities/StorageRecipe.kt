package com.jurassicspb.recipes_firebase.storage.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = "recipes",
    indices = [Index("id")])
data class StorageRecipe(
    @PrimaryKey val id: Long,
    val name: String
)