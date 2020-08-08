package com.dgnt.quickTournamentMaker.data.tournament

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RankScore(val win:Int, val loss:Int, val tie:Int) : Parcelable ,IRankConfig{
    override val stringTripleRepresentation= Triple(win.toString(),loss.toString(),tie.toString())
}