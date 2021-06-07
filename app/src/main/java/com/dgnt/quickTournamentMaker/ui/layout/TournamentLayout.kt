package com.dgnt.quickTournamentMaker.ui.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
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


    private val roundWidth = context.resources.getDimension(R.dimen.round_width).toInt()
    private val roundMargin = context.resources.getDimension(R.dimen.round_margin).toInt()

    private fun getName(participant: Participant) =
        when (participant.participantType) {
            ParticipantType.NORMAL -> participant.displayName
            ParticipantType.NULL -> ""
            else -> context.getString(R.string.byeDefaultName)
        }

    fun drawTournament(roundGroup: List<RoundGroup>, clickListener: (MatchUp, View,View, ParticipantPosition) -> Unit) {
        roundGroup.forEach { rg ->
            addView(LinearLayout(context).also { rgll ->
                rgll.orientation = HORIZONTAL
                rg.rounds.forEach { r ->
                    rgll.addView(LinearLayout(context).also { rll ->
                        rll.orientation = VERTICAL
                        rll.layoutParams = LayoutParams(roundWidth, LayoutParams.WRAP_CONTENT).also { it.setMargins(roundMargin, roundMargin, roundMargin, roundMargin) }
                        r.matchUps.forEach { mu ->
                            rll.addView(SimpleMatchUpLayoutBinding.inflate(LayoutInflater.from(context)).also { mul ->
                                mul.player1.apply {
                                    text = getName(mu.participant1)
                                    setOnClickListener {
                                        clickListener(mu, this, mul.player2, ParticipantPosition.P1)
                                    }
                                }
                                mul.player2.apply {
                                    text = getName(mu.participant2)
                                    setOnClickListener {
                                        clickListener(mu, mul.player1, this, ParticipantPosition.P2)
                                    }
                                }

                            }.root)

                        }
                    })
                }
            })
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