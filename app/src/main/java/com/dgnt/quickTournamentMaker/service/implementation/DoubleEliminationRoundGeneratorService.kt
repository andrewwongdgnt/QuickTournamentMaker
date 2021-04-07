package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService

class DoubleEliminationRoundGeneratorService(private val roundGeneratorService: IRoundGeneratorService) : IRoundGeneratorService {
    override fun build(orderedParticipants: List<Participant>, roundNamer: (Round) -> String): List<RoundGroup> {

        val winnersBracket = roundGeneratorService.build(orderedParticipants, roundNamer)[0]

        val loserBracket = RoundGroup(1, winnersBracket.rounds
            .map { it.matchUps.size / 2 }
            .dropLast(1)
            .mapIndexed { i, matchUpTotal ->
                (0..1).map { m ->
                    val roundIndex = i * 2 + m
                    (Round(1, roundIndex, (0 until matchUpTotal)
                        .map { matchUpIndex -> MatchUp(1, roundIndex, matchUpIndex, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT) })
                            )
                        .apply {
                            title = roundNamer(this)
                        }
                }
            }.flatten()
        )

        val finalBracket = RoundGroup(
            2,
            listOf(
                Round(2, 0, listOf(MatchUp(2, 0, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT))),//mandatory round
                Round(2, 1, listOf(MatchUp(2, 1, 0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)))//extra round
            )
        )

        return listOf(winnersBracket, loserBracket, finalBracket)
    }

}