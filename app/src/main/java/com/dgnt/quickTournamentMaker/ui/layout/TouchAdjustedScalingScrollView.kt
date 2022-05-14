package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import com.dgnt.quickTournamentMaker.util.SimpleLogger
import com.moagrius.widget.ScalingScrollView
import com.moagrius.widget.ScalingScrollView.ScaleChangedListener

class TouchAdjustedScalingScrollView : ScalingScrollView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var hasSingleFingerDown: Boolean = false
    private var currentScale = 1.0f

    private val mScaleChangedListener = ScaleChangedListener { _, currentScale, previousScale ->
        this.currentScale = currentScale
    }

    init {
        scaleChangedListener = mScaleChangedListener
    }

    override fun onPointerCounterChange(pointerCount: Int) {
        hasSingleFingerDown = pointerCount == 1
        super.onPointerCounterChange(pointerCount)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        SimpleLogger.d(this, "action: ${event.action}")
        if (hasSingleFingerDown) {
            val matrix = Matrix()
            val mappingMatrix = Matrix()
            matrix.setScale(currentScale, currentScale)
//            matrix.postTranslate(-scrollX*(1-scale), -scrollY*(1-scale))
            matrix.postTranslate(-scrollX*(1-scale), -scrollY*(1-scale))

            matrix.invert(mappingMatrix)
            SimpleLogger.d(this, "x: $scrollX, y: $scrollY")
//            SimpleLogger.d(this, mappingMatrix.toShortString())

            val transformEvent = MotionEvent.obtain(event)
            transformEvent.transform(mappingMatrix)

            val returnVal = super.dispatchTouchEvent(transformEvent)

            transformEvent.recycle()
            return returnVal
        } else {
            SimpleLogger.d(this, "x: $scrollX, y: $scrollY")
            return super.dispatchTouchEvent(event)
        }
    }
}