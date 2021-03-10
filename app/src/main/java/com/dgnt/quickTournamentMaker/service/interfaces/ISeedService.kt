package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.Participant

interface ISeedService {
    /**
     * Generate the seeding
     *
     * @param participants the list of participants
     * @return list of participants
     */
    fun seed(participants: List<Participant>) : List<Participant>
}