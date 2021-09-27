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

    val rounds = roundGroups.flatMap { it.rounds }

    val matchUps = rounds.flatMap { it.matchUps }

    fun getMatchUpsWithSingleByes() = matchUps.filter { it.containsBye(true) }

    val participants = roundGroups[0].rounds[0].matchUps.flatMap { listOf(it.participant1, it.participant2) }.filter { it.participantType == ParticipantType.NORMAL }.sorted()

    fun updateRound(roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int) = roundUpdateService.update(roundGroups, roundGroupIndex, roundIndex, matchUpIndex, tournamentInformation.rankConfig)

    fun getRanking() = rankingService.calculate(roundGroups, tournamentInformation.rankConfig)

}
