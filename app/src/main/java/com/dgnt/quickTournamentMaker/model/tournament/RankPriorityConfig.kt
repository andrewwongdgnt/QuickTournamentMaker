package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.lang.IllegalArgumentException

enum class RankPriorityConfigType(val shorthand: String) {
    WIN("w"), LOSS("l"), TIE("t");

    companion object {
        fun fromString(value: String):RankPriorityConfigType {
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
    }

    override val stringTripleRepresentation = Triple(first.shorthand, second.shorthand, third.shorthand)

}