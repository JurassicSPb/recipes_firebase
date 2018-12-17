package com.jurassicspb.recipes_firebase.storage.dao.mappers

import com.jurassicspb.recipes_firebase.storage.entities.StorageFavorite
import tools.Mapper

class FavoritesIdToStorageFavoriteMapper : Mapper<Long, StorageFavorite>() {

    override fun map(from: Long) = StorageFavorite(from)
}