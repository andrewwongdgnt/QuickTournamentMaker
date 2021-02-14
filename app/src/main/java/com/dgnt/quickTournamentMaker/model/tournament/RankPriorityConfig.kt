package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

enum class RankPriorityConfigType(val shorthand: String) {
    WIN("w"), LOSS("l"), TIE("t");

    companion object {
        fun fromString(value: String): RankPriorityConfigType {
            return when (value) {
                WIN.shorthand -> WIN
                LOSS.shorthand -> LOSS
                TIE.shorthand -> TIE
                else -> throw  IllegalArgumentException("RankPriorityConfigType cannot be derived from $value")
            }
        }
    }
}

@Parcelize
data class RankPriorityConfig(val first: RankPriorityConfigType, val second: RankPriorityConfigType, val third: RankPriorityConfigType) : Parcelable, IRankConfig {
    companion object {
        const val DEFAULT_INPUT = "w;l;t"
        val DEFAULT = RankPriorityConfig(RankPriorityConfigType.WIN, RankPriorityConfigType.LOSS, RankPriorityConfigType.TIE)
    }

    override val stringTripleRepresentation = Triple(first.shorthand, second.shorthand, third.shorthand)
    val tripleRepresentation = Triple(first, second, third)

}