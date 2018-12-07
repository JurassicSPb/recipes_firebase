package com.jurassicspb.recipes_firebase.recipes

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.jurassicspb.recipes_firebase.R
import com.jurassicspb.recipes_firebase.base.BaseActivity
import com.jurassicspb.recipes_firebase.login.SingleEvent
import com.jurassicspb.recipes_firebase.model.RecipeItem
import kotlinx.android.synthetic.main.activity_recipes.*
import org.koin.android.ext.android.inject


class RecipesActivity : BaseActivity<RecipeState, RecipeViewModel>(RecipeViewModel::class),
    RecipeListItemTouchHelper.OnStartDragListener {

    override val layoutRes = R.layout.activity_recipes

    private val diffUtilCallback = RecipeDiffUtilsCallback()
    private lateinit var itemTouchHelper: ItemTouchHelper
    private val recipesAdapter: RecipesAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recipesAdapter.apply {
            recyclerView = recipes_list
            startDragListener = this@RecipesActivity
        }

        recipes_list.apply {
            layoutManager = LinearLayoutManager(this@RecipesActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecipesItemDecoration(this@RecipesActivity))
            adapter = recipesAdapter
            itemTouchHelper = ItemTouchHelper(RecipeListItemTouchHelper(recipesAdapter)).apply {
                attachToRecyclerView(recipes_list)
            }
        }
    }

    override fun applyChanges(it: RecipeState) {
        updateRecipes(it.recipeItems)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private fun updateRecipes(recipeItems: SingleEvent<List<RecipeItem>>) {
        diffUtilCallback.apply {
            oldData = recipesAdapter.items
            newData = recipeItems.value
        }
        val productDiffResult = DiffUtil.calculateDiff(diffUtilCallback)
        recipesAdapter.items = recipeItems.value.toMutableList()
        productDiffResult.dispatchUpdatesTo(recipesAdapter)
    }
}