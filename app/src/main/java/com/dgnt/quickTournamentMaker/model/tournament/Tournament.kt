package com.dgnt.quickTournamentMaker.model.tournament

import com.dgnt.quickTournamentMaker.R
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpStatusTransformService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService

enum class TournamentType(val resource: Int) {
    ELIMINATION(R.string.elimination),
    DOUBLE_ELIMINATION(R.string.doubleElimination),
    ROUND_ROBIN(R.string.roundRobin),
    SWISS(R.string.swiss),
    SURVIVAL(R.string.survival)
}

enum class SeedType(val resource: Int) {
    RANDOM(R.string.randomSeed),
    CUSTOM(R.string.customSeed),
    SAME(R.string.sameSeed)
}

data class Tournament(
    val tournamentInformation: TournamentInformation,
    val roundGroups: List<RoundGroup>,
    val matchUpStatusTransformService: IMatchUpStatusTransformService,
    val roundUpdateService: IRoundUpdateService,
    val rankingService: IRankingService
) {

    val rounds = roundGroups.flatMap { it.rounds }

    val matchUps = rounds.flatMap { it.matchUps }

    fun getMatchUpsWithSingleByes() = matchUps.filter { it.containsBye(true) }

    val orderedParticipants = roundGroups[0].rounds[0].matchUps.flatMap { listOf(it.participant1, it.participant2) }.filter { it.participantType == ParticipantType.NORMAL }

    fun updateRound(roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int) = roundUpdateService.update(roundGroups, roundGroupIndex, roundIndex, matchUpIndex, tournamentInformation.rankConfig)

    fun getRanking() = rankingService.calculate(roundGroups, tournamentInformation.rankConfig)

}
