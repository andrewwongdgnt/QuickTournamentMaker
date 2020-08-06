package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.Round

interface IParticipantService {

    /**
     * create a round with a list of participants
     *
     * @param participants the participants
     * @return the round
     */
    fun createRound(participants: List<Participant>): Round

}