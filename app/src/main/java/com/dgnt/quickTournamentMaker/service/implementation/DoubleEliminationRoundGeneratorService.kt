package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService

class DoubleEliminationRoundGeneratorService(
    private val roundGeneratorService: IRoundGeneratorService
) : IRoundGeneratorService {
    override fun build(
        orderedParticipants: List<Participant>,
        defaultRoundGroupTitleFunc: (Int) -> String,
        defaultRoundTitleFunc: (Int) -> String,
        defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String
    ): List<RoundGroup> {

        val winnersBracket = roundGeneratorService.build(orderedParticipants, defaultRoundGroupTitleFunc, defaultRoundTitleFunc, defaultMatchUpTitleFunc)[0]

        val loserBracket = RoundGroup(
            1,
            winnersBracket.rounds
                .map { it.matchUps.size / 2 }
                .dropLast(1)
                .mapIndexed { i, matchUpTotal ->
                    (0..1).map { m ->
                        val roundIndex = i * 2 + m
                        Round(
                            1,
                            roundIndex,
                            (0 until matchUpTotal)
                                .map { matchUpIndex ->
                                    MatchUp(
                                        1,
                                        roundIndex,
                                        matchUpIndex,
                                        Participant.NULL_PARTICIPANT,
                                        Participant.NULL_PARTICIPANT,
                                        defaultMatchUpTitleFunc(matchUpIndex, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                                    )
                                },
                            defaultRoundTitleFunc(roundIndex)
                        )
                    }
                }.flatten(),
            defaultRoundGroupTitleFunc(1)
        )

        val finalBracket = RoundGroup(
            2,
            listOf(
                //mandatory round
                Round(
                    2,
                    0,
                    listOf(
                        MatchUp(
                            2,
                            0,
                            0,
                            Participant.NULL_PARTICIPANT,
                            Participant.NULL_PARTICIPANT,
                            defaultMatchUpTitleFunc(0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                        )
                    ),
                    defaultRoundTitleFunc(0)
                ),
                //extra round
                Round(
                    2,
                    1,
                    listOf(
                        MatchUp(
                            2,
                            1,
                            0,
                            Participant.NULL_PARTICIPANT,
                            Participant.NULL_PARTICIPANT,
                            defaultMatchUpTitleFunc(0, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                        )
                    ),
                    defaultRoundTitleFunc(1)
                )
            ),
            defaultRoundGroupTitleFunc(2)
        )

        return listOf(winnersBracket, loserBracket, finalBracket)
    }

}