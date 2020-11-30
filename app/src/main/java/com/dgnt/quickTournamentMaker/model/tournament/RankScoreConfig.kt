package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RankScoreConfig(val win:Int, val loss:Int, val tie:Int) : Parcelable ,IRankConfig{

    companion object {
        const val DEFAULT_INPUT = "2;0;1"
    }
    override val stringTripleRepresentation= Triple(win.toString(),loss.toString(),tie.toString())
}