package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService

class SwissRoundUpdateService(recordRankingService: IRankingService) : IRoundUpdateService {
    override fun update(roundGroups: List<RoundGroup>, roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int) {

        // Purge
    }
}