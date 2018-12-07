package com.jurassicspb.recipes_firebase.model

data class RecipeItem(val id: Long, val name: String, var isFavorite: Boolean = false)