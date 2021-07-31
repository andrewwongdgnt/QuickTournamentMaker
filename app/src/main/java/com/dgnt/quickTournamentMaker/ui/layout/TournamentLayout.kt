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

class TournamentLayout : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        setWillNotDraw(false)
    }

    private val map = mutableMapOf<Triple<Int, Int, Int>, SimpleMatchUpLayoutBinding>()

    private val roundWidth = context.resources.getDimension(R.dimen.round_width).toInt()
    private val roundMargin = context.resources.getDimension(R.dimen.round_margin).toInt()

    private fun getName(participant: Participant) =
        when (participant.participantType) {
            ParticipantType.NORMAL -> participant.displayName
            ParticipantType.NULL -> ""
            else -> context.getString(R.string.byeDefaultName)
        }

    fun draw(tournament: Tournament, clickListener: (MatchUp, ParticipantPosition) -> Unit) {
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
                                map[mu.key] = mul
                            }.root)

                        }
                    })
                }
            })
        }
        updateMatchUps(tournament.matchUps)
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
            map[it.key]?.apply {
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
            restore()
        }
    }
}