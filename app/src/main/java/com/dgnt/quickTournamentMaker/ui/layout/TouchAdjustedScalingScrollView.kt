package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
import com.dgnt.quickTournamentMaker.util.SimpleLogger
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import com.moagrius.widget.ScalingScrollView
import com.moagrius.widget.ScalingScrollView.ScaleChangedListener

class TouchAdjustedScalingScrollView : ScalingScrollView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private val staticExtraHeight by lazy { TournamentUtil.getStaticExtraHeight(context.theme, resources) }
    private fun dynamicExtraHeight() = (context as? TournamentActivity)?.extraLayoutHeight() ?: 0
    private fun extraHeight() = dynamicExtraHeight() + staticExtraHeight

    var allViews: List<View> = listOf()
    private var currentScale = 1.0f

    init {
        scaleChangedListener = ScaleChangedListener { _, currentScale, _ ->
            this.currentScale = currentScale
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        val matrix = Matrix()
        val mappingMatrix = Matrix()
        matrix.setScale(currentScale, currentScale)
        matrix.preTranslate(0f, -extraHeight().toFloat())
        matrix.postTranslate(-scrollX * (1 - scale), -scrollY * (1 - scale))

        matrix.invert(mappingMatrix)

        val transformEvent = MotionEvent.obtain(event)
        transformEvent.transform(mappingMatrix)

        if (event.action == MotionEvent.ACTION_UP){
            val text = (allViews.find { intercept(transformEvent, it) } as? TextView)?.text
            SimpleLogger.d(this, "view: $text")
        }

        return super.dispatchTouchEvent(event)

    }

    private fun intercept(ev: MotionEvent?, view: View?): Boolean {
        try {
            if (ev != null && view != null) {
                val coords = IntArray(2)
                view.getLocationOnScreen(coords)
                if (ev.x >= coords[0].toFloat() && ev.x <= coords[0] + view.width.toFloat()) {
                    if (ev.y >= coords[1].toFloat() && ev.y <= coords[1] + view.height.toFloat()) return true
                }
            }
        } catch (e: Exception) {
        }
        return false
    }
}