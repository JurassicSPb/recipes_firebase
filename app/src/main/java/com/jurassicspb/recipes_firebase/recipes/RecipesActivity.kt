package com.jurassicspb.recipes_firebase.recipes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.jurassicspb.recipes_firebase.R
import kotlinx.android.synthetic.main.activity_recipes.*
import org.koin.android.ext.android.inject


class RecipesActivity : AppCompatActivity(), RecipeListItemTouchHelper.OnStartDragListener {

    private val diffUtilCallback = RecipeDiffUtilsCallback()
    private lateinit var itemTouchHelper: ItemTouchHelper
    private val recipesAdapter: RecipesAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recipes)

        recipesAdapter.apply {
            recyclerView = recipes_list
            startDragListener = this@RecipesActivity
        }

        recipes_list.apply {
            layoutManager = LinearLayoutManager(this@RecipesActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(RecipesItemDecoration(this@RecipesActivity))
            adapter = recipesAdapter.apply {
                items = mutableListOf(
                    RecipeItem(0, "0"),
                    RecipeItem(1, "1"),
                    RecipeItem(2, "2"),
                    RecipeItem(3, "30", true)
                )
                notifyDataSetChanged()
            }
            itemTouchHelper = ItemTouchHelper(RecipeListItemTouchHelper(recipesAdapter)).apply {
                attachToRecyclerView(recipes_list)
            }
        }

        update_recipes_button.setOnClickListener {
            updateFavorites()
        }
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private fun updateFavorites() {
        val updatedList = mutableListOf(RecipeItem(0, "0", true), RecipeItem(1, "3"), RecipeItem(2, "4"))
        diffUtilCallback.apply {
            oldData = recipesAdapter.items
            newData = updatedList
        }
        val productDiffResult = DiffUtil.calculateDiff(diffUtilCallback)
        recipesAdapter.items = updatedList
        productDiffResult.dispatchUpdatesTo(recipesAdapter)
    }
}