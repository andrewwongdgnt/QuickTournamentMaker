package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.Round
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService

class DoubleEliminationRoundGeneratorService(private val roundGeneratorService: IRoundGeneratorService) : IRoundGeneratorService {
    override fun build(orderedParticipants: List<Participant>): List<RoundGroup> {

        val winnersBracket = roundGeneratorService.build(orderedParticipants)[0]

        val loserBracket = RoundGroup(winnersBracket.rounds
            .map { it.matchUps.size / 2 }
            .dropLast(1)
            .map { matchUpTotal ->
                (1..2).map {
                    (Round((1..matchUpTotal)
                        .map { MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT) })
                            )
                }
            }.flatten()
        )

        val finalBracket = RoundGroup(
            listOf(
                Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),//mandatory round
                Round(listOf(MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))//extra round
            )
        )

        return listOf(winnersBracket, loserBracket, finalBracket)
    }

}