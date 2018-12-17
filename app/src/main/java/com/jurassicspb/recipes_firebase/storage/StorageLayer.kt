package com.jurassicspb.recipes_firebase.storage

import com.jurassicspb.recipes_firebase.model.RecipeItem
import com.jurassicspb.recipes_firebase.storage.dao.mappers.FavoritesIdToStorageFavoriteMapper
import com.jurassicspb.recipes_firebase.storage.dao.mappers.RecipeItemToStorageRecipeMapper
import com.jurassicspb.recipes_firebase.storage.dao.mappers.StorageRecipeToRecipeItemMapper
import com.jurassicspb.recipes_firebase.storage.entities.StorageRecipe
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.functions.BiFunction

class StorageLayer(
    private val appDataBase: AppDataBase,
    private val recipeItemMapper: RecipeItemToStorageRecipeMapper,
    private val storageRecipeMapper: StorageRecipeToRecipeItemMapper,
    private val favoritesMapper: FavoritesIdToStorageFavoriteMapper
) {

    fun saveRecipes(
        items: List<RecipeItem>,
        favIds: List<Long>,
        message: String?
    ): Completable {
        return Completable.fromAction {
            appDataBase.recipeDao().insert(recipeItemMapper.map(items))
            if (message != null && message != "") {
                appDataBase.recipeFavoritesDao().deleteAll()
                if (!favIds.isEmpty()) {
                    appDataBase.recipeFavoritesDao().insert(favoritesMapper.map(favIds))
                }
            }
        }
    }

    fun getRecipes(): Maybe<List<RecipeItem>> =
        Maybe.defer {
            Maybe.zip(appDataBase.recipeDao().getAll(),
                appDataBase.recipeFavoritesDao().getAll(),
                BiFunction { recipes: List<StorageRecipe>, favorites: List<Long> ->
                    storageRecipeMapper.map(
                        recipes,
                        favorites
                    )
                }
            )
        }
}