package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.math.truncate

@Parcelize
data class Progress(
    val matchesCompleted: Int,
    val totalMatches: Int
) : Parcelable, Comparable<Progress> {
    // gives us a value between 0 - 100 with truncated 2 decimal places
    val percentageRep: Float = truncate(matchesCompleted.toFloat() / totalMatches.toFloat() * 10000) / 100
    override fun compareTo(other: Progress) =
        percentageRep.compareTo(other.percentageRep)

}