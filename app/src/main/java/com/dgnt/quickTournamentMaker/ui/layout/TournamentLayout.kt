package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.DoubleEliminationConfigurationBinding
import com.dgnt.quickTournamentMaker.databinding.SimpleMatchUpLayoutBinding
import com.dgnt.quickTournamentMaker.databinding.SingleMatchUpLayoutBinding
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity
import com.dgnt.quickTournamentMaker.util.TournamentUtil
import com.dgnt.quickTournamentMaker.util.viewBinding

private const val CURVATURE = 15

class TournamentLayout : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        setWillNotDraw(false)
    }

    var offsetGetter: () -> Point = { Point(0, 0) }
    private val matchUpViewMap = mutableMapOf<Triple<Int, Int, Int>, SimpleMatchUpLayoutBinding>()
    private val singleMatchUpViewMap = mutableMapOf<Triple<Int, Int, Int>, SingleMatchUpLayoutBinding>()

    private val roundWidth = context.resources.getDimension(R.dimen.round_width).toInt()
    private val roundMargin = context.resources.getDimension(R.dimen.round_margin).toInt()

    private var tournament: Tournament? = null
    private var nextMatchUpKeyFunc: ((Triple<Int, Int, Int>) -> Triple<Int, Int, Int>)? = null

    private fun getName(participant: Participant) =
        when (participant.participantType) {
            ParticipantType.NORMAL -> participant.getDisplayName()
            ParticipantType.NULL -> ""
            else -> context.getString(R.string.byeDefaultName)
        }

    private val staticExtraHeight by lazy { TournamentUtil.getStaticExtraHeight(context.theme, resources) }
    private fun dynamicExtraHeight() = (context as? TournamentActivity)?.extraLayoutHeight() ?: 0
    private fun extraHeight() = dynamicExtraHeight() + staticExtraHeight

    fun draw(
        tournament: Tournament,
        clickListener: (MatchUp, ParticipantPosition) -> Unit
    ): List<Pair<TextView, ()->Unit>> {
        this.tournament = tournament
        val allViews = mutableListOf<Pair<TextView, ()->Unit>>()

        removeAllViews()

        val roundGroupLayouts = tournament.roundGroups.map { rg ->
            LinearLayout(context).also { rgll ->
                rgll.orientation = HORIZONTAL
                rg.rounds.forEach { r ->
                    rgll.addView(LinearLayout(context).also { rll ->
                        rll.orientation = VERTICAL
                        rll.gravity = CENTER
                        rll.layoutParams = LayoutParams(roundWidth, LayoutParams.MATCH_PARENT).also { it.setMargins(roundMargin, roundMargin, roundMargin, roundMargin) }
                        r.matchUps.forEach { mu ->
                            val view = if (tournament.tournamentInformation.tournamentType == TournamentType.SURVIVAL) {
                                viewBinding(SingleMatchUpLayoutBinding::inflate).also { mul ->
                                    mul.layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
                                    mul.player.apply {
                                        text = getName(mu.participant1)
                                        setTextColor(mu.participant1.color)
                                    }.also {
                                        allViews.add(it to {
                                            clickListener(mu, ParticipantPosition.P1)
                                            updateViews(mu, mul)
                                            updateSingleMatchUps(tournament.matchUps)
                                        })
                                    }
                                    updateViews(mu, mul)
                                    singleMatchUpViewMap[mu.key] = mul
                                }.root
                            } else {
                                viewBinding(SimpleMatchUpLayoutBinding::inflate).also { mul ->
                                    mul.layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
                                    mul.player1.apply {
                                        text = getName(mu.participant1)
                                        setTextColor(mu.participant1.color)
                                    }.also {
                                        allViews.add(it to {
                                            if (!mu.containsBye()) {
                                                clickListener(mu, ParticipantPosition.P1)
                                                updateViews(mu, mul)
                                                updateMatchUps(tournament.matchUps)
                                            }
                                        })
                                    }
                                    mul.player2.apply {
                                        text = getName(mu.participant2)
                                        setTextColor(mu.participant2.color)
                                    }.also{
                                        allViews.add(it to {
                                            if (!mu.containsBye()) {
                                                clickListener(mu, ParticipantPosition.P2)
                                                updateViews(mu, mul)
                                                updateMatchUps(tournament.matchUps)
                                            }
                                        })
                                    }
                                    updateViews(mu, mul)
                                    matchUpViewMap[mu.key] = mul
                                }.root
                            }
                            rll.addView(view)
                        }
                    })
                }
            }
        }

        if (tournament.roundGroups.size == 1) {
            roundGroupLayouts.forEach {
                addView(it)
            }
        } else if (tournament.tournamentInformation.tournamentType == TournamentType.DOUBLE_ELIMINATION) {
            addView(viewBinding(DoubleEliminationConfigurationBinding::inflate).also { binding ->
                binding.winnerBracket.addView(roundGroupLayouts[0])
                binding.loserBracket.addView(roundGroupLayouts[1])
                binding.finalBracket.addView(roundGroupLayouts[2])
            }.root)
        }

        if (tournament.tournamentInformation.tournamentType == TournamentType.SURVIVAL) {
            updateSingleMatchUps(tournament.matchUps)
        } else {
            updateMatchUps(tournament.matchUps)
        }

        nextMatchUpKeyFunc = when (tournament.tournamentInformation.tournamentType) {
            TournamentType.ELIMINATION -> {
                { current -> Triple(0, current.second + 1, current.third / 2) }
            }
            TournamentType.DOUBLE_ELIMINATION -> {
                { current ->
                    when (current.first) {
                        0 -> {
                            if (current.second == tournament.roundGroups[current.first].rounds.size - 1) {
                                Triple(current.first + 2, 0, 0)
                            } else {
                                Triple(current.first, current.second + 1, current.third / 2)
                            }
                        }
                        1 -> {
                            if (current.second == tournament.roundGroups[current.first].rounds.size - 1) {
                                Triple(current.first + 1, 0, 0)
                            } else {
                                Triple(current.first, current.second + 1, if (current.second % 2 == 0) current.third else current.third / 2)
                            }
                        }
                        else -> {

                            Triple(current.first, current.second + 1, current.third)
                        }
                    }


                }
            }
            else -> null
        }
        invalidate()
        return allViews
    }

    private fun updateViews(matchUp: MatchUp, matchUpLayout: SimpleMatchUpLayoutBinding) {
        when (matchUp.status) {
            MatchUpStatus.P1_WINNER -> {
                matchUpLayout.player1.background = ContextCompat.getDrawable(context, R.drawable.p1_win)
                matchUpLayout.player2.background = ContextCompat.getDrawable(context, R.drawable.p2_default)
            }
            MatchUpStatus.P2_WINNER -> {
                matchUpLayout.player1.background = ContextCompat.getDrawable(context, R.drawable.p1_default)
                matchUpLayout.player2.background = ContextCompat.getDrawable(context, R.drawable.p2_win)
            }
            MatchUpStatus.TIE -> {
                matchUpLayout.player1.background = ContextCompat.getDrawable(context, R.drawable.p1_win)
                matchUpLayout.player2.background = ContextCompat.getDrawable(context, R.drawable.p2_win)
            }
            else -> {
                matchUpLayout.player1.background = ContextCompat.getDrawable(context, R.drawable.p1_default)
                matchUpLayout.player2.background = ContextCompat.getDrawable(context, R.drawable.p2_default)
            }
        }
    }

    private fun updateViews(matchUp: MatchUp, matchUpLayout: SingleMatchUpLayoutBinding) {
        when (matchUp.status) {
            MatchUpStatus.P1_WINNER -> {
                matchUpLayout.player.background = ContextCompat.getDrawable(context, R.drawable.p_general_win)
            }
            else -> {
                matchUpLayout.player.background = ContextCompat.getDrawable(context, R.drawable.p_general_default)
            }
        }
    }

    private fun updateMatchUps(matchUps: List<MatchUp>) {
        matchUps.forEach {
            matchUpViewMap[it.key]?.apply {
                player1.text = getName(it.participant1)
                player2.text = getName(it.participant2)
            }
        }
    }

    private fun updateSingleMatchUps(matchUps: List<MatchUp>) {
        matchUps.forEach {
            singleMatchUpViewMap[it.key]?.apply {
                player.text = getName(it.participant1)
            }
        }
    }

    private val mainPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = ResourcesCompat.getColor(resources, R.color.defaultStroke, null)
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        nextMatchUpKeyFunc?.let { func ->
            canvas.apply {
                save()

                matchUpViewMap.forEach {
                    matchUpViewMap[func(it.key)]?.let { next ->
                        val current = it.value
                        val currentCoordinates = intArrayOf(0, 0)
                        current.point.getLocationOnScreen(currentCoordinates)

                        val nextCoordinates = intArrayOf(0, 0)
                        next.point.getLocationOnScreen(nextCoordinates)

                        drawCurve(
                            canvas,
                            (currentCoordinates[0] + current.point.width).toFloat() + offsetGetter().x,
                            currentCoordinates[1].toFloat() - extraHeight() + offsetGetter().y,
                            nextCoordinates[0].toFloat() + offsetGetter().x,
                            nextCoordinates[1].toFloat() - extraHeight() + offsetGetter().y
                        )
                    }
                }

                restore()
            }
        }
    }

    private fun drawCurve(canvas: Canvas, startX: Float, startY: Float, endX: Float, endY: Float) {

        val midPointX = (startX + endX) / 2
        val cornerRadius = CURVATURE

        canvas.drawLine(startX, startY, (midPointX - cornerRadius), startY, mainPaint)

        val mPath = Path()
        mPath.moveTo((midPointX - cornerRadius), startY)
        mPath.quadTo(midPointX, startY, midPointX, (startY + cornerRadius * endY.compareTo(startY)))

        canvas.drawLine(midPointX, (startY + cornerRadius * endY.compareTo(startY)), midPointX, endY, mainPaint)
        canvas.drawLine(midPointX, endY, endX, endY, mainPaint)

        canvas.drawPath(mPath, mainPaint)
    }


}