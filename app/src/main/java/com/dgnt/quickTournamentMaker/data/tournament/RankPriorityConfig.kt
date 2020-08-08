package com.dgnt.quickTournamentMaker.data.tournament

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class RankPriorityConfigType(val shorthand:String) {
    WIN("w"), LOSS("l"),TIE("t")
}

@Parcelize
data class RankPriorityConfig(val first:RankPriorityConfigType, val second:RankPriorityConfigType, val third:RankPriorityConfigType) : Parcelable, IRankConfig {
    override val stringTripleRepresentation= Triple(first.shorthand,second.shorthand,third.shorthand)
}