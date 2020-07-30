package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService


class DoubleEliminationRoundUpdateService(private val roundUpdateService: IRoundUpdateService) : IRoundUpdateService {
    override fun update(roundGroups: List<RoundGroup>, roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int) {

        val roundGroup = roundGroups[roundGroupIndex]

        //winner bracket
        if (roundGroupIndex == 0) {
            roundUpdateService.update(roundGroups, roundGroupIndex, roundIndex, matchUpIndex)

            var currentRoundIndexWinnerBracket = roundIndex;
            var currentMatchUpIndexWinnerBracket = matchUpIndex;
            while (currentRoundIndexWinnerBracket < roundGroups[roundGroupIndex].rounds.size - 1) {

                val currentRoundWinnerBracket = roundGroup.rounds[currentRoundIndexWinnerBracket]
                val currentMatchUpWinnerBracket = currentRoundWinnerBracket.matchUps[currentMatchUpIndexWinnerBracket]

                val currentRoundIndexLoserBracket = if (currentRoundIndexWinnerBracket == 0) 0 else currentRoundIndexWinnerBracket * 2 - 1
                val currentRoundLoserBracket = roundGroups[1].rounds[currentRoundIndexLoserBracket]

                val currentMatchUpIndexLoserBracket = when {
                    currentRoundIndexWinnerBracket == 0 -> currentMatchUpIndexWinnerBracket / 2
                    currentRoundIndexWinnerBracket % 2 == 1 -> currentRoundLoserBracket.matchUps.size - currentMatchUpIndexWinnerBracket - 1
                    else -> currentMatchUpIndexWinnerBracket
                }

                val currentMatchUpLoserBracket = currentRoundLoserBracket.matchUps[currentMatchUpIndexLoserBracket];

                //if this is not the first round, then we always move the loser of the winner bracket match up to the participant 1 position of the corresponding loser bracket match up
                //otherwise, we alternate

                val newParticipant = when (currentMatchUpWinnerBracket.status) {
                    MatchUpStatus.P1_WINNER -> currentMatchUpWinnerBracket.participant2;
                    MatchUpStatus.P2_WINNER -> currentMatchUpWinnerBracket.participant1;
                    else -> Participant.NULL_PARTICIPANT;
                }

                if (currentRoundIndexWinnerBracket != 0 || currentMatchUpIndexWinnerBracket % 2 == 0) {
                    if (currentMatchUpLoserBracket.participant1 != newParticipant)
                        currentMatchUpLoserBracket.participant1 = newParticipant;
                    else
                        break;
                } else {
                    if (currentMatchUpLoserBracket.participant2 != newParticipant)
                        currentMatchUpLoserBracket.participant2 = newParticipant;
                    else
                        break;
                }

                update(roundGroups, 1, currentRoundIndexLoserBracket, currentMatchUpIndexLoserBracket);

                if (currentRoundIndexWinnerBracket==roundGroup.rounds.size-1){
                    update(roundGroups, 2, 0, 0);

                }
                currentRoundIndexWinnerBracket++
                currentMatchUpIndexWinnerBracket /= 2


            }
        }

        //loser bracket
        else if (roundGroupIndex == 1) {

            var currentRoundIndex = roundIndex;
            var currentMatchUpIndex = matchUpIndex;
            var continueLoop = true;
            while (continueLoop && currentRoundIndex < roundGroups[roundGroupIndex].rounds.size - 1) {

                val currentRound = roundGroup.rounds[currentRoundIndex]

                val currentMatchUp = currentRound.matchUps[currentMatchUpIndex]

                val futureRoundIndex = currentRoundIndex + 1
                val futureRound = roundGroup.rounds[futureRoundIndex]

                val futureMatchUpIndex = if (futureRoundIndex % 2 == 0) currentMatchUpIndex / 2 else currentMatchUpIndex
                val futureMatchUp = futureRound.matchUps[futureMatchUpIndex]

                val updateFutureMatchUp: (Participant) -> Boolean = {
                    val differentParticipant: Boolean
                    if (currentRoundIndex % 2 == 0 || currentMatchUpIndex % 2 == 1) {
                        differentParticipant = futureMatchUp.participant1.key != it.key
                        futureMatchUp.participant2 = it
                    } else {
                        differentParticipant = futureMatchUp.participant2.key != it.key
                        futureMatchUp.participant1 = it
                    }

                    differentParticipant
                }

                continueLoop = updateFutureMatchUp(
                    when (currentMatchUp.status) {
                        MatchUpStatus.P1_WINNER -> currentMatchUp.participant1
                        MatchUpStatus.P2_WINNER -> currentMatchUp.participant2
                        else -> Participant.NULL_PARTICIPANT
                    }
                )

                currentRoundIndex = futureRoundIndex
                currentMatchUpIndex = futureMatchUpIndex
            }
        } else if (roundGroupIndex == 2) {

        }
    }
}