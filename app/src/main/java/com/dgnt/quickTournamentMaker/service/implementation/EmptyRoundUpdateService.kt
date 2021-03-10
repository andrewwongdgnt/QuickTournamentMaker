package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.IRankConfig
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService

/**
 * Exists to do nothing so we don't need to come up with a null service or have logic otherwise
 *
 */
class EmptyRoundUpdateService:IRoundUpdateService {
    override fun update(roundGroups: List<RoundGroup>, roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int, rankConfig: IRankConfig) {
        //Supposed to do nothing
    }
}