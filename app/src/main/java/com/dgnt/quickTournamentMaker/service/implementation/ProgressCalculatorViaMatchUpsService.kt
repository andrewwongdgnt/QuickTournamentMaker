package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IProgressCalculatorService

class ProgressCalculatorViaMatchUpsService: IProgressCalculatorService {
    override fun calculate(tournament: Tournament) =
        tournament.run {
            Progress(matchUps.count { it.completed() }, getNumOpenMatchUps())
        }

    private fun MatchUp.completed() =
        this.participant1.participantType == ParticipantType.NORMAL &&
                this.participant2.participantType == ParticipantType.NORMAL &&
                this.status != MatchUpStatus.DEFAULT
}