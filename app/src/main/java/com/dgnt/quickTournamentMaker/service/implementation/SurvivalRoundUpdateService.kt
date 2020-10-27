package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.IRankConfig
import com.dgnt.quickTournamentMaker.model.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService

class SurvivalRoundUpdateService : IRoundUpdateService {
    override fun update(roundGroups: List<RoundGroup>, roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int, rankConfig: IRankConfig) {

        val roundGroup = roundGroups[roundGroupIndex]

        var currentRoundIndex = roundIndex;
        var continueLoop = true;
        while (continueLoop && currentRoundIndex < roundGroups[roundGroupIndex].rounds.lastIndex) {

            val currentRound = roundGroup.rounds[currentRoundIndex]

            val currentMatchUp = currentRound.matchUps[matchUpIndex]

            val futureRoundIndex = currentRoundIndex + 1
            val futureRound = roundGroup.rounds[futureRoundIndex]

            val futureMatchUp = futureRound.matchUps[matchUpIndex]

            val updateFutureMatchUp: (Participant) -> Boolean = {

                val differentParticipant = futureMatchUp.participant1.key != it.key
                futureMatchUp.participant1 = it
                differentParticipant
            }

            continueLoop = updateFutureMatchUp(
                when (currentMatchUp.status) {
                    MatchUpStatus.P1_WINNER -> currentMatchUp.participant1
                    else -> Participant.NULL_PARTICIPANT
                }
            )

            currentRoundIndex = futureRoundIndex
        }
    }
}