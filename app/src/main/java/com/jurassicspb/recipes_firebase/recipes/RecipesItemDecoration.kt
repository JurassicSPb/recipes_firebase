package com.jurassicspb.recipes_firebase.recipes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jurassicspb.recipes_firebase.R
import com.jurassicspb.recipes_firebase.extensions.drawableOrDefault

class RecipesItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val marginStart = context.resources.getDimensionPixelSize(R.dimen.item_decoration_margin_start)
    private val divider = context.drawableOrDefault(R.drawable.item_decorator)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + marginStart
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as? RecyclerView.LayoutParams

            val top = child.bottom + (params?.bottomMargin ?: 0)
            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.bottom = divider.intrinsicHeight
    }
}