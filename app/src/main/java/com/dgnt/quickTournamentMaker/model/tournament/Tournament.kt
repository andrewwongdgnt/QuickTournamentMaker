package com.dgnt.quickTournamentMaker.model.tournament

import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService

enum class TournamentType {
    ELIMINATION, DOUBLE_ELIMINATION, ROUND_ROBIN, SWISS, SURVIVAL
}

enum class SeedType {
    RANDOM, CUSTOM, SAME
}

data class Tournament(
    val tournamentInformation: TournamentInformation,
    val roundGroups: List<RoundGroup>,
    val matchUpStatusTransformService: IMatchUpStatusTransformService,
    val roundUpdateService: IRoundUpdateService,
    val rankingService: IRankingService
) {

    val matchUps = roundGroups.flatMap {
        it.rounds.flatMap { it.matchUps.map { it } }
    }

    val rounds = roundGroups.flatMap { it.rounds }

    fun updateRound(roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int) = roundUpdateService.update(roundGroups, roundGroupIndex, roundIndex, matchUpIndex, tournamentInformation.rankConfig)

    fun getRanking() = rankingService.calculate(roundGroups, tournamentInformation.rankConfig)

}
