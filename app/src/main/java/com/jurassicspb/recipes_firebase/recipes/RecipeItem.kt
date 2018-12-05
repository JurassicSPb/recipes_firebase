package com.jurassicspb.recipes_firebase.recipes

data class RecipeItem(val id: Long, val name: String, var isFavorite: Boolean = false)