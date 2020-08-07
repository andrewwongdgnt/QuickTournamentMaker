package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IEliminationRankingHelperService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService

class DoubleEliminationRankingService (private val eliminationRankingHelperService:IEliminationRankingHelperService): IRankingService {
    override fun calculate(roundGroups: List<RoundGroup>)=eliminationRankingHelperService.calculate(roundGroups.first().rounds,roundGroups[1].rounds,roundGroups.last().rounds)

}