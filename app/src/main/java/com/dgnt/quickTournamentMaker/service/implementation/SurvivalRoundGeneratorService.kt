package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService

class SurvivalRoundGeneratorService(private val participantService: IParticipantService) : IRoundGeneratorService {
    override fun build(orderedParticipants: List<Participant>): List<RoundGroup> {

        val round1 = participantService.createRound(orderedParticipants)
        val totalParticipants = orderedParticipants.size / 2
        val rounds = (1 until totalParticipants).map {
            when (it) {
                1 -> round1
                else -> Round(
                    (1..totalParticipants).map { MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT) }
                )
            }
        }

        return listOf(RoundGroup(rounds))
    }
}