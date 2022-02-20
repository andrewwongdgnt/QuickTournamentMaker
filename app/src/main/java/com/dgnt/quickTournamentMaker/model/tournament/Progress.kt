package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.math.truncate

@Parcelize
data class Progress(
    val matchesCompleted: Int,
    val totalMatches: Int
): Parcelable
{
    val percentageRep: Float = truncate(matchesCompleted.toFloat() / totalMatches.toFloat() * 100) / 100

    override fun toString() = "$percentageRep"


}