package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.android.parcel.Parcelize
import java.util.*

enum class TournamentType {
    ELIMINATION, DOUBLE_ELIMINATION, ROUND_ROBIN, SWISS, SURVIVAL
}

enum class SeedType {
    RANDOM,CUSTOM,SAME
}
@Parcelize
data class TournamentInformation(val title: String, val description: String, val persons:List<String>, val tournamentType:TournamentType, val seedType:SeedType, val rankConfig:IRankConfig, val creationDate:Date, val lastModifiedDate:Date?=null): Parcelable

data class Tournament(val tournamentInformation: TournamentInformation) {
}
