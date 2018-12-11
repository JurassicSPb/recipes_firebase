package com.jurassicspb.recipes_firebase.storage.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey

@Entity(
    tableName = "favorites",
    foreignKeys = [ForeignKey(
        entity = StorageRecipe::class,
        parentColumns = ["id"],
        childColumns = ["favId"],
        onDelete = CASCADE
    )]
)
class StorageFavorite(@PrimaryKey val favId: Long)