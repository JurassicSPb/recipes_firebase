package com.jurassicspb.recipes_firebase.storage.dao.mappers

import com.jurassicspb.recipes_firebase.model.RecipeItem
import com.jurassicspb.recipes_firebase.storage.entities.StorageRecipe
import tools.Mapper

class StorageRecipeToRecipeItemMapper : Mapper<StorageRecipe, RecipeItem>() {

    // todo: zip with favorite ids
    override fun map(from: StorageRecipe) = RecipeItem(from.id, from.name)
}