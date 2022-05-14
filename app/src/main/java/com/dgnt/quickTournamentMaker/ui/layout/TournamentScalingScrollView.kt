package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.graphics.Matrix
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
import com.dgnt.quickTournamentMaker.util.SimpleLogger
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import com.dgnt.quickTournamentMaker.util.distance
import com.dgnt.quickTournamentMaker.util.isIntercepted
import com.moagrius.widget.ScalingScrollView
import com.moagrius.widget.ScalingScrollView.ScaleChangedListener
import kotlin.math.abs

class TournamentScalingScrollView : ScalingScrollView {


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    companion object {
        private const val SCALE_TOLERANCE = 0.03f
        private const val MOVE_TOLERANCE = 3
    }

    private val staticExtraHeight by lazy { TournamentUtil.getStaticExtraHeight(context.theme, resources) }
    private fun dynamicExtraHeight() = (context as? TournamentActivity)?.extraLayoutHeight() ?: 0
    private fun extraHeight() = dynamicExtraHeight() + staticExtraHeight

    var allViews: List<Pair<TextView, () -> Unit>> = listOf()
    private var currentScale = 1.0f
    private var scaleOnFirstTouch = 1.0f
    private var coordinatesOnFirstTouch = Point(0, 0)

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

        if (event.action == MotionEvent.ACTION_DOWN) {
            scaleOnFirstTouch = currentScale
            coordinatesOnFirstTouch = Point(scrollX, scrollY)
        }


        if (event.action == MotionEvent.ACTION_UP) {

            val scaleDiff = abs(scaleOnFirstTouch - currentScale)
            val moveDiff = coordinatesOnFirstTouch.distance(Point(scrollX, scrollY))

            allViews
                .takeIf { scaleDiff <= SCALE_TOLERANCE }
                ?.takeIf { moveDiff <= MOVE_TOLERANCE }
                ?.find { it.first.isIntercepted(transformEvent) }
                ?.let { player ->
                    SimpleLogger.d(this, "view: ${player.first.text}")
                    player.second()
                }
        }

        return super.dispatchTouchEvent(event)

    }

}