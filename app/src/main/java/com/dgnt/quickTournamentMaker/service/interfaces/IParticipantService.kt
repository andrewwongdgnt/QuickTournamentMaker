package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round

interface IParticipantService {

    /**
     * create a round with a list of participants
     *
     * @param participants the participants
     * @param roundGroupIndex the round group index (assume it is 0)
     * @param roundIndex the round index (assume it is 0)
     * @return the round
     */
    fun createRound(participants: List<Participant>, roundGroupIndex: Int = 0, roundIndex: Int = 0, roundNamer: (Round) -> String = { r -> r.roundIndex.toString() }): Round

}