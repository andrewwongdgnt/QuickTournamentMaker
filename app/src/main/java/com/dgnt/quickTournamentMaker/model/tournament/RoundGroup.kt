package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.model.IKeyable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoundGroup(
    val roundGroupIndex: Int,
    val rounds: List<Round>,
    val title: String
) : IKeyable<Int>, Parcelable {

    override val key = roundGroupIndex
}