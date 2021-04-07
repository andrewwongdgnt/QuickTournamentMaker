package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup

interface IRoundGeneratorService {

    /**
     * build tournament brackets
     *
     * @param orderedParticipants participants in the order they were defined
     * @return List of round groups
     */
    fun build(orderedParticipants: List<Participant>, roundNamer: (Round) -> String = {r -> r.roundIndex.toString()}): List<RoundGroup>

}