package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService

class SurvivalRoundGeneratorService(
    private val participantService: IParticipantService
) : IRoundGeneratorService {
    override fun build(
        orderedParticipants: List<Participant>,
        defaultRoundGroupTitleFunc: (Int) -> String,
        defaultRoundTitleFunc: (Int) -> String,
        defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String
    ): List<RoundGroup> {

        val round1 = participantService.createRound(orderedParticipants, defaultRoundTitleFunc = defaultRoundTitleFunc, defaultMatchUpTitleFunc = defaultMatchUpTitleFunc)
        val totalParticipants = orderedParticipants.size / 2
        val rounds = (0 until totalParticipants - 1).map { roundIndex ->

            when (roundIndex) {
                0 -> round1
                else -> Round(
                    0,
                    roundIndex,
                    (0 until totalParticipants).map {
                        MatchUp(
                            0,
                            roundIndex,
                            it,
                            Participant.NULL_PARTICIPANT,
                            Participant.NULL_PARTICIPANT,
                            defaultMatchUpTitleFunc(it, Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
                        )
                    },
                    defaultRoundTitleFunc(roundIndex)
                )
            }
        }

        return listOf(RoundGroup(0, rounds, defaultRoundGroupTitleFunc(0)))
    }
}