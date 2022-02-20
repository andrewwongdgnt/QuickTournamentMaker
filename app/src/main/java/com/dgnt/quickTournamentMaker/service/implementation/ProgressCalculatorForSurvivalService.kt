package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IProgressCalculatorService

class ProgressCalculatorForSurvivalService : IProgressCalculatorService {
    override fun calculate(tournament: Tournament) =
        tournament.run {
            Progress(rounds.count { it.completed() }, rounds.size)
        }

    private fun Round.completed() = matchUps.all { it.completed() }

    private fun MatchUp.completed() =
        this.participant1.participantType == ParticipantType.NORMAL &&
                this.status != MatchUpStatus.DEFAULT
}