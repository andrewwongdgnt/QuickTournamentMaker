package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import com.moagrius.widget.ScalingScrollView
import com.moagrius.widget.ScalingScrollView.ScaleChangedListener

class TouchAdjustedScalingScrollView : ScalingScrollView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var currentScale = 1.0f

    private val mScaleChangedListener = ScaleChangedListener { _, currentScale, previousScale ->
        this.currentScale = currentScale
    }

    init {
        scaleChangedListener = mScaleChangedListener
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val matrix = Matrix()
        val mappingMatrix = Matrix()
        //TODO handle panning
        matrix.setScale(currentScale, currentScale)
        matrix.invert(mappingMatrix)

        val transformEvent = MotionEvent.obtain(event)
        transformEvent.transform(mappingMatrix)

        val returnVal = super.dispatchTouchEvent(transformEvent)

        transformEvent.recycle()
        return returnVal
    }

}