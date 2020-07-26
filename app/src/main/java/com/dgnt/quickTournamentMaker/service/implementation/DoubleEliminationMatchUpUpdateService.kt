package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IMatchUpUpdateService

class DoubleEliminationMatchUpUpdateService(private val eliminationMatchUpUpdateService: EliminationMatchUpUpdateService) : IMatchUpUpdateService {
    override fun update(roundGroups: List<RoundGroup>, roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int, currentMatchUpStatus: MatchUpStatus, newMatchUpStatus: MatchUpStatus) {

        val roundGroup = roundGroups[roundGroupIndex]
        roundGroup.rounds[roundIndex].matchUps[matchUpIndex].status = newMatchUpStatus

        //winner bracket
        eliminationMatchUpUpdateService.update(roundGroups,roundGroupIndex,roundIndex,matchUpIndex,currentMatchUpStatus,newMatchUpStatus)

        //loser bracket

    }
}