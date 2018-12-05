package com.jurassicspb.recipes_firebase.util

import android.animation.ObjectAnimator
import android.view.View
import com.jurassicspb.recipes_firebase.extensions.toAnimator

class AnimationHelper {
    private var fadeIn: ObjectAnimator? = null
    private var fadeOut: ObjectAnimator? = null

    fun fadeOut(view: View?) {
        fadeOut = view?.toAnimator(View.ALPHA, MAX_ALPHA, TRANSITION_DURATION)
        fadeOut?.start()
    }

    fun fadeIn(view: View?) {
        fadeIn = view?.toAnimator(View.ALPHA, MIN_ALPHA, TRANSITION_DURATION)
        fadeIn?.start()
    }

    companion object {
        private const val MIN_ALPHA = 0f
        private const val MAX_ALPHA = 1f
        private const val TRANSITION_DURATION = 200L
    }
}