package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IByeStatusResolverService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInitiatorService

class TournamentInitiatorService(private val byeStatusResolverService: IByeStatusResolverService) : ITournamentInitiatorService {
    override fun initiate(tournament: Tournament) {
        when (tournament.tournamentInformation.tournamentType) {
            TournamentType.ROUND_ROBIN -> tournament.rounds.map { Pair(it.roundGroupIndex, it.roundIndex) }
            TournamentType.ELIMINATION, TournamentType.SWISS -> listOf(Pair(0, 0))
            TournamentType.DOUBLE_ELIMINATION -> listOf(Pair(0, 0), Pair(1, 0), Pair(1, 1))
            else -> emptyList()
        }.forEach { pair ->
            byeStatusResolverService.resolve(tournament.roundGroups, pair)
            (tournament.roundGroups.getOrNull(pair.first)?.rounds?.getOrNull(pair.second)?.matchUps?.indices)?.forEach {
                tournament.updateRound(pair.first, pair.second, it)
            }
        }
    }
}