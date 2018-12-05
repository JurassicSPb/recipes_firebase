package com.jurassicspb.recipes_firebase.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.jurassicspb.recipes_firebase.R
import com.jurassicspb.recipes_firebase.extensions.dimenPx

class MovingRectangleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val rectangleData = RectangleData()
    private var touched = false
    private val squareSizePx = context.dimenPx(R.dimen.square_size)
    private val halfSquareSizePx = squareSizePx / 2
    private var restored = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (rectangleData.update(measuredWidth, measuredHeight) && !restored) {
            val centerX = measuredWidth.div(2f)
            val centerY = measuredHeight.div(2f)
            rectangleData.initRectangle(
                centerX - halfSquareSizePx,
                centerY - halfSquareSizePx,
                centerX + halfSquareSizePx,
                centerY + halfSquareSizePx
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (!rectangleData.isOnDestPoint() && touched) {
            canvas.drawRect(rectangleData.rectangle, paint)
            rectangleData.updateRectangleX()
            rectangleData.updateRectangleY()
            invalidate()
        } else {
            canvas.drawRect(rectangleData.rectangle, paint)
            touched = false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pointerIndex = event.actionIndex
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> return true
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                rectangleData.destPointX = event.getX(pointerIndex)
                rectangleData.destPointY = event.getY(pointerIndex)
                if (!rectangleData.isOnDestPoint()) {
                    invalidate()
                    touched = true
                }
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    inner class RectangleData(private var width: Int = 0, private var height: Int = 0) {
        val rectangle = RectF()
        var top = 0f
        var bottom = 0f
        var left = 0f
        var right = 0f
        var destPointX = -1f
        var destPointY = -1f

        fun update(width: Int, height: Int) = if (this.width != width || this.height != height) {
            this.width = width
            this.height = height
            true
        } else
            false

        fun initRectangle(left: Float, top: Float, right: Float, bottom: Float) {
            this.top = top
            this.bottom = bottom
            this.left = left
            this.right = right
            rectangle.set(left, top, right, bottom)
        }

        fun updateRectangleX() {
            if (destPointX < left) {
                if (destPointX > left - SPEED) {
                    val diff = destPointX - left
                    left = destPointX
                    right += diff
                    rectangle.set(left, top, right, bottom)
                    return
                }
                left -= SPEED
                right -= SPEED
                rectangle.set(left, top, right, bottom)
            } else if (destPointX > right) {
                if (destPointX < right + SPEED) {
                    val diff = right - destPointX
                    left -= diff
                    right = destPointX
                    rectangle.set(left, top, right, bottom)
                    return
                }
                left += SPEED
                right += SPEED
                rectangle.set(left, top, right, bottom)
            }
        }

        fun updateRectangleY() {
            if (destPointY < top) {
                if (destPointY > top - SPEED) {
                    val diff = destPointY - top
                    top = destPointY
                    bottom += diff
                    rectangle.set(left, top, right, bottom)
                    return
                }
                top -= SPEED
                bottom -= SPEED
                rectangle.set(left, top, right, bottom)
            } else if (destPointY > bottom) {
                if (destPointY < bottom + SPEED) {
                    val diff = bottom - destPointY
                    top -= diff
                    bottom = destPointY
                    rectangle.set(left, top, right, bottom)
                    return
                }
                top += SPEED
                bottom += SPEED
                rectangle.set(left, top, right, bottom)
            }
        }

        fun isOnDestPoint(): Boolean {
            return !(destPointX < left || destPointX > right) &&
                    !(destPointY < top || destPointY > bottom)
        }

        fun isOnDestPointH(): Boolean {
            return !(destPointX < left || destPointX > right) &&
                    !(destPointY < top || destPointY > bottom)
        }
    }

    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
        putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
        putBoolean(SUPER_TOUCHED_KEY, touched)
        rectangleData.let {
            putFloatArray(
                SUPER_COORDS_KEY,
                floatArrayOf(it.left, it.top, it.right, it.bottom, it.destPointX, it.destPointY)
            )
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state !is Bundle) {
            super.onRestoreInstanceState(state)
            return
        }
        restored = true
        touched = state.getBoolean(SUPER_TOUCHED_KEY)
        rectangleData.apply {
            state.getFloatArray(SUPER_COORDS_KEY)?.let {
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    initRectangle(it[0], it[1], it[2], it[3])
                    destPointX = it[4]
                    destPointY = it[5]
                } else {
                    initRectangle(it[3], it[2], it[1], it[0])
                    destPointX = it[5]
                    destPointY = it[4]
                }
            }
        }

        super.onRestoreInstanceState(state.getParcelable(SUPER_STATE_KEY))
    }

    companion object {
        private const val SPEED = 20
        private const val SUPER_STATE_KEY = "superKey"
        private const val SUPER_TOUCHED_KEY = "touchedKey"
        private const val SUPER_COORDS_KEY = "coordsKey"
    }
}