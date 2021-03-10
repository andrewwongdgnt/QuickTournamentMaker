package com.dgnt.quickTournamentMaker.model.tournament

import android.os.Parcelable
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService
import kotlinx.android.parcel.Parcelize
import java.util.*

enum class TournamentType {
    ELIMINATION, DOUBLE_ELIMINATION, ROUND_ROBIN, SWISS, SURVIVAL
}

enum class SeedType {
    RANDOM, CUSTOM, SAME
}

@Parcelize
data class TournamentInformation(val title: String, val description: String, val participants: List<Participant>, val tournamentType: TournamentType, val seedType: SeedType, val rankConfig: IRankConfig, val creationDate: Date, val lastModifiedDate: Date? = null) : Parcelable

data class Tournament(val tournamentInformation: TournamentInformation, val roundGroups: List<RoundGroup>, val roundUpdateService: IRoundUpdateService, val rankingService: IRankingService, val matchUpStatusTransformService: IMatchUpStatusTransformService) {

    val matchUps = roundGroups.flatMap {
        it.rounds.flatMapIndexed { i, r -> r.matchUps.map { Pair(i, it) } }
    }

}
