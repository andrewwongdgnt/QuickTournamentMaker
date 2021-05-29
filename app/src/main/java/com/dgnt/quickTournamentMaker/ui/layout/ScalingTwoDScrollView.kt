package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.animation.ScaleAnimation

class ScalingTwoDScrollView : TwoDScrollView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var container:TournamentLayout? = null

    fun setSomething(container: TournamentLayout){
        this.container = container
    }

    private var mScaleFactor = 1f
    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val prevScale: Float = mScaleFactor

            mScaleFactor *= detector.scaleFactor

            // Don't let the object get too small or too large.
            mScaleFactor = mScaleFactor.coerceIn(0.1f, 5.0f)

            val scaleAnimation = ScaleAnimation(prevScale, mScaleFactor,  prevScale,  mScaleFactor, detector.focusX, detector.focusY)
            scaleAnimation.duration = 0
            scaleAnimation.fillAfter = true
            container?.startAnimation(scaleAnimation)
            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        super.dispatchTouchEvent(ev)
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev)
        return true
    }
}