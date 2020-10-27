package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService
import java.util.*

class SwissRoundGeneratorService(private val participantService: IParticipantService) : IRoundGeneratorService {
    override fun build(orderedParticipants: List<Participant>): List<RoundGroup> {

        val round1 = participantService.createRound(orderedParticipants)
        val rounds = ArrayList<Round>()
        rounds.add(round1)
        for (i in 2 until orderedParticipants.size) {

            rounds.add(Round((orderedParticipants.indices step 2).map {
                MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT)
            }))
        }
        return listOf(RoundGroup(rounds))

    }
}