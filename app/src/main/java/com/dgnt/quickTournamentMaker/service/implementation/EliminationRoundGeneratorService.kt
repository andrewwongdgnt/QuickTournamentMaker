package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService

class EliminationRoundGeneratorService(private val participantService: IParticipantService) : IRoundGeneratorService {
    override fun build(orderedParticipants: List<Participant>, defaultRoundTitleFunc: (Round) -> String, defaultMatchUpTitleFunc: (MatchUp) -> String): List<RoundGroup> {

        val round1 = participantService.createRound(orderedParticipants, defaultRoundTitleFunc = defaultRoundTitleFunc, defaultMatchUpTitleFunc = defaultMatchUpTitleFunc)

        val rounds: MutableList<Round> = mutableListOf()
        rounds.add(round1)

        val totalParticipants = orderedParticipants.size

        var matchUpTotal: Double = totalParticipants / 4.0
        var roundIndex = 1;
        while (matchUpTotal >= 1) {

            val round = Round(0, roundIndex, (0 until matchUpTotal.toInt())
                .map { matchUpIndex ->
                    MatchUp(0, roundIndex, matchUpIndex, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                        .apply { title = defaultMatchUpTitleFunc(this) }
                })
                .apply {
                    title = defaultRoundTitleFunc(this)
                }
            rounds.add(round)

            matchUpTotal *= 0.5
            roundIndex++
        }

        return listOf(RoundGroup(0, rounds))
    }

}