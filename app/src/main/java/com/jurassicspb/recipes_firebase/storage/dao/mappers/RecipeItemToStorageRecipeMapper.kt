package com.jurassicspb.recipes_firebase.storage.dao.mappers

import com.jurassicspb.recipes_firebase.model.RecipeItem
import com.jurassicspb.recipes_firebase.storage.entities.StorageRecipe
import tools.Mapper

class RecipeItemToStorageRecipeMapper : Mapper<RecipeItem, StorageRecipe>() {

    override fun map(from: RecipeItem): StorageRecipe = StorageRecipe(from.id, from.name)
}