package com.jurassicspb.recipes_firebase.recipes

import com.jurassicspb.recipes_firebase.base.BaseViewModel
import com.jurassicspb.recipes_firebase.extensions.execute
import com.jurassicspb.recipes_firebase.model.RecipeItem
import com.jurassicspb.recipes_firebase.repository.Repository

class RecipeViewModel(private val repository: Repository) : BaseViewModel<RecipeState>(RecipeState()) {

    override fun onOwnerCreated(restoring: Boolean) {
        if (!restoring) {
            createItems()

            repository.favIdsResult().execute(compositeDisposable,
                onNext = {
                    data.value.recipeItems.value = it.favorites
                    data.notifyObservers()
                },
                onError = {
                    println("ERROR $it")
                })
        }
    }

    private fun createItems() {
        val list = mutableListOf(
            RecipeItem(0, "0"),
            RecipeItem(1, "1"),
            RecipeItem(2, "2"),
            RecipeItem(3, "3")
        )

        repository.saveRecipeItems(list).execute(compositeDisposable,
            onError = {
                println("ERROR2 $it")
            })
    }
}