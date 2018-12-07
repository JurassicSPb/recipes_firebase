package com.jurassicspb.recipes_firebase.storage

import com.jurassicspb.recipes_firebase.model.RecipeItem
import com.jurassicspb.recipes_firebase.storage.dao.mappers.RecipeItemToStorageRecipeMapper
import com.jurassicspb.recipes_firebase.storage.dao.mappers.StorageRecipeToRecipeItemMapper
import io.reactivex.Completable
import io.reactivex.Maybe

class StorageLayer(
    private val appDataBase: AppDataBase,
    private val recipeItemMapper: RecipeItemToStorageRecipeMapper,
    private val storageRecipeMapper: StorageRecipeToRecipeItemMapper
) {

    fun saveRecipes(items: List<RecipeItem>): Completable {
        return Completable.fromAction {
            appDataBase.recipeDao().insert(recipeItemMapper.map(items))
        }
    }

    fun getRecipes(): Maybe<List<RecipeItem>> =
        appDataBase.recipeDao().getAll()
            .map { storageRecipeMapper.map(it) }
}