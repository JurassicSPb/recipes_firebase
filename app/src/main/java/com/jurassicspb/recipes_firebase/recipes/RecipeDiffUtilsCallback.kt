package com.jurassicspb.recipes_firebase.recipes

import android.support.v7.util.DiffUtil

class RecipeDiffUtilsCallback : DiffUtil.Callback() {
    var oldData: List<RecipeItem> = ArrayList()
    var newData: List<RecipeItem> = ArrayList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition].id == newData[newItemPosition].id

    override fun getOldListSize() = oldData.size

    override fun getNewListSize() = newData.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldData[oldItemPosition].name == newData[newItemPosition].name &&
                oldData[oldItemPosition].isFavorite == newData[newItemPosition].isFavorite
}