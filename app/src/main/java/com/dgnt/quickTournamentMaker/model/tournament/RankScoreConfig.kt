package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RankScoreConfig(val win: Float, val loss: Float, val tie: Float) : Parcelable, IRankConfig {

    companion object {
        const val DEFAULT_INPUT = "1.0;0.0;0.5"
    }

    override val stringTripleRepresentation = Triple(win.toString(), loss.toString(), tie.toString())
}