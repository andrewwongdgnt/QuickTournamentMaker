package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.Rank
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IEliminationRankingHelperService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService

class EliminationRankingService(private var eliminationRankingHelperService: IEliminationRankingHelperService) : IRankingService {
    override fun calculate(roundGroups: List<RoundGroup>): Rank {
        val rounds = roundGroups.first().rounds
        return eliminationRankingHelperService.calculate(rounds, rounds.dropLast(1), rounds.takeLast(1))
    }


}