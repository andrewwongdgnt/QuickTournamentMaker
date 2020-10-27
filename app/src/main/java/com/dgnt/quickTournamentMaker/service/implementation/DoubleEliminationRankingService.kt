package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.IRankConfig
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IEliminationRankingHelperService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService

class DoubleEliminationRankingService(private val eliminationRankingHelperService: IEliminationRankingHelperService) : IRankingService {
    override fun calculate(roundGroups: List<RoundGroup>, rankConfig: IRankConfig) = eliminationRankingHelperService.calculate(roundGroups.first().rounds, roundGroups[1].rounds, roundGroups.last().rounds)

}