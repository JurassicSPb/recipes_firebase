package com.jurassicspb.recipes_firebase.recipes

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class RecipeListItemTouchHelper(private val adapter: RecipesAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) =  adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun isLongPressDragEnabled() = true

    override fun isItemViewSwipeEnabled() = true

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE &&
                viewHolder is RecipesAdapter.ItemViewHolder) {
            adapter.onItemSelected(viewHolder)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is RecipesAdapter.ItemViewHolder) {
            adapter.onItemCleared(viewHolder)
        }
    }

    interface ItemTouchHelperAdapter {

        fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

        fun onItemDismiss(position: Int)

        fun onItemSelected(viewHolder: RecyclerView.ViewHolder)

        fun onItemCleared(viewHolder: RecyclerView.ViewHolder)
    }

    interface OnStartDragListener {

        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }
}