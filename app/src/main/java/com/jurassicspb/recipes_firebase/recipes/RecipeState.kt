package com.jurassicspb.recipes_firebase.recipes

import com.jurassicspb.recipes_firebase.base.BaseState
import com.jurassicspb.recipes_firebase.login.SingleEvent
import com.jurassicspb.recipes_firebase.model.RecipeItem

class RecipeState : BaseState() {

    val recipeItems = SingleEvent(emptyList<RecipeItem>())
}