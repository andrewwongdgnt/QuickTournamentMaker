package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.IRankConfig
import com.dgnt.quickTournamentMaker.model.tournament.Rank
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IEliminationRankingHelperService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService

class EliminationRankingService(private var eliminationRankingHelperService: IEliminationRankingHelperService) : IRankingService {
    override fun calculate(roundGroups: List<RoundGroup>, rankConfig: IRankConfig): Rank {
        val rounds = roundGroups.first().rounds
        return eliminationRankingHelperService.calculate(rounds, rounds.dropLast(1), rounds.takeLast(1))
    }


}