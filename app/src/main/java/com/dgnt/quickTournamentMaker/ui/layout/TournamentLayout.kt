package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.databinding.SimpleMatchUpLayoutBinding
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.ui.tournament.TournamentActivity


class TournamentLayout : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        setWillNotDraw(false)
    }

    private val matchUpViewMap = mutableMapOf<Triple<Int, Int, Int>, SimpleMatchUpLayoutBinding>()

    private val roundWidth = context.resources.getDimension(R.dimen.round_width).toInt()
    private val roundMargin = context.resources.getDimension(R.dimen.round_margin).toInt()

    private var tournament: Tournament? = null

    private fun getName(participant: Participant) =
        when (participant.participantType) {
            ParticipantType.NORMAL -> participant.displayName
            ParticipantType.NULL -> ""
            else -> context.getString(R.string.byeDefaultName)
        }

    private val staticExtraHeight by lazy {
        // action bar
        val styledAttributes = context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val actionBarSize = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()

        //title bar
        var titleBarSize = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            titleBarSize = resources.getDimensionPixelSize(resourceId)
        }

        actionBarSize + titleBarSize
    }
    private fun dynamicExtraHeight() = (context as? TournamentActivity)?.extraLayoutHeight() ?: 0
    private fun extraHeight() = dynamicExtraHeight() + staticExtraHeight

    fun draw(tournament: Tournament, clickListener: (MatchUp, ParticipantPosition) -> Unit) {
        this.tournament = tournament
        tournament.roundGroups.forEach { rg ->
            addView(LinearLayout(context).also { rgll ->
                rgll.orientation = HORIZONTAL
                rg.rounds.forEach { r ->
                    rgll.addView(LinearLayout(context).also { rll ->
                        rll.orientation = VERTICAL
                        rll.gravity = CENTER
                        rll.layoutParams = LayoutParams(roundWidth, LayoutParams.MATCH_PARENT).also { it.setMargins(roundMargin, roundMargin, roundMargin, roundMargin) }
                        r.matchUps.forEach { mu ->
                            rll.addView(SimpleMatchUpLayoutBinding.inflate(LayoutInflater.from(context)).also { mul ->
                                mul.layout.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
                                mul.player1.apply {
                                    text = getName(mu.participant1)
                                    if (!mu.containsBye) {
                                        setOnClickListener {
                                            clickListener(mu, ParticipantPosition.P1)
                                            updateViews(mu, mul)
                                            updateMatchUps(tournament.matchUps)
                                        }
                                    }
                                }
                                mul.player2.apply {
                                    text = getName(mu.participant2)
                                    if (!mu.containsBye) {
                                        setOnClickListener {
                                            clickListener(mu, ParticipantPosition.P2)
                                            updateViews(mu, mul)
                                            updateMatchUps(tournament.matchUps)
                                        }
                                    }
                                }
                                updateViews(mu, mul)
                                matchUpViewMap[mu.key] = mul
                            }.root)
                        }
                    })
                }
            })
        }
        updateMatchUps(tournament.matchUps)
        invalidate()
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


    private fun updateMatchUps(matchUps: List<MatchUp>) {


        matchUps.forEach {
            matchUpViewMap[it.key]?.apply {
                player1.text = getName(it.participant1)
                player2.text = getName(it.participant2)
            }
        }
    }


    private val shadowPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 2f
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        canvas.apply {
            save()

            val nextMatchUpKey: ((Triple<Int, Int, Int>) -> Triple<Int, Int, Int>)? = when (tournament?.tournamentInformation?.tournamentType) {
                TournamentType.ELIMINATION -> {
                    { current -> Triple(0, current.second + 1, current.third / 2) }
                }
                TournamentType.DOUBLE_ELIMINATION -> {
                    { current ->
                        when (current.first) {
                            0 -> Triple(current.first, current.second + 1, current.third / 2)
                            1 -> {

                                Triple(current.first, current.second + 1, if (current.second % 2 == 0) current.third else current.third / 2)
                            }
                            else -> {

                                Triple(current.first, current.second + 1, current.third)
                            }
                        }


                    }
                }
                else -> null
            }

            nextMatchUpKey?.let { func ->
                matchUpViewMap.map {
                    matchUpViewMap[func(it.key)]?.let { next ->
                        val current = it.value
                        val currentCoordinates = intArrayOf(0, 0)
                        current.point.getLocationOnScreen(currentCoordinates)

                        val nextCoordinates = intArrayOf(0, 0)
                        next.point.getLocationOnScreen(nextCoordinates)

                        drawLine((currentCoordinates[0] + current.point.width).toFloat(), currentCoordinates[1].toFloat() - extraHeight(), nextCoordinates[0].toFloat(), nextCoordinates[1].toFloat() - extraHeight(), shadowPaint)
                    }
                }
            }

            restore()
        }
    }


}