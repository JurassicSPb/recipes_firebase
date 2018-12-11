package com.jurassicspb.recipes_firebase.storage.dao.mappers

import com.jurassicspb.recipes_firebase.model.RecipeItem
import com.jurassicspb.recipes_firebase.storage.entities.StorageRecipe
import tools.Mapper

class StorageRecipeToRecipeItemMapper : Mapper<StorageRecipe, RecipeItem>() {

    override fun map(from: StorageRecipe) = RecipeItem(from.id, from.name)

    private fun map(recipe: StorageRecipe, favorites: List<Long>) = map(recipe).apply {
        this.isFavorite = favorites.contains(this.id)
    }

    fun map(recipes: List<StorageRecipe>, favorites: List<Long>) = recipes.map {
        map(it, favorites)
    }
}