package com.jurassicspb.recipes_firebase.recipes

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jurassicspb.recipes_firebase.R
import com.jurassicspb.recipes_firebase.extensions.color
import com.jurassicspb.recipes_firebase.extensions.swap
import com.jurassicspb.recipes_firebase.util.AnimationHelper
import java.util.*

class RecipesAdapter(val animationHelper: AnimationHelper, private val context: Context) :
    RecyclerView.Adapter<RecipesAdapter.ItemViewHolder>(),
    RecipeListItemTouchHelper.ItemTouchHelperAdapter {

    private val transition = TransitionSet().addTransition(ChangeBounds()).addTransition(Fade())
        .setOrdering(TransitionSet.ORDERING_TOGETHER)

    var items: MutableList<RecipeItem> = ArrayList()
        set(value) {
            if (field != value) {
                field = value
                TransitionManager.beginDelayedTransition(recyclerView?.parent as? ViewGroup, transition)
            }
        }

    var recyclerView: RecyclerView? = null
    var startDragListener: RecipeListItemTouchHelper.OnStartDragListener? = null

    override fun getItemCount() = items.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val holder = ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false))
        holder.handle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                startDragListener?.onStartDrag(holder)
            }
            false
        }
        return holder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) = with(holder) {
        val recipe = items[position]

        name.text = recipe.name
        isFavorite.let {
            if (recipe.isFavorite) it.setImageResource(R.drawable.ic_favorite) else it.setImageResource(R.drawable.ic_not_favorite)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        items.swap(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        items.removeAt(position)
        TransitionManager.beginDelayedTransition(recyclerView?.parent as? ViewGroup, transition)
        notifyItemRemoved(position)
    }

    override fun onItemSelected(viewHolder: RecyclerView.ViewHolder) {
        animationHelper.fadeOut((viewHolder as? ItemViewHolder)?.innerContainer)
//        (viewHolder as? ItemViewHolder)?.outerContainer?.setBackgroundColor(context.color(R.color.colorGrey600))
    }

    override fun onItemCleared(viewHolder: RecyclerView.ViewHolder) {
        animationHelper.fadeIn((viewHolder as? ItemViewHolder)?.innerContainer)
//        (viewHolder as? ItemViewHolder)?.outerContainer?.setBackgroundColor(context.color(R.color.colorGrey300))
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val handle: ImageView = itemView.findViewById(R.id.handle)
        val name: TextView = itemView.findViewById(R.id.recipe_item_name)
        val isFavorite: ImageView = itemView.findViewById(R.id.recipe_item_favorite)
        val outerContainer: View = itemView.findViewById(R.id.recipe_item_outer_container)
        val innerContainer: View = itemView.findViewById(R.id.recipe_item_inner_container)
    }
}