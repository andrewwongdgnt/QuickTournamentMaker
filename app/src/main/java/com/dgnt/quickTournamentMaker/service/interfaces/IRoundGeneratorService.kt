package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.Round
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup

interface IRoundGeneratorService {

    /**
     * build tournament brackets
     *
     * @param orderedParticipants participants in the order they were defined
     * @return List of round groups
     */
    fun build(orderedParticipants: List<Participant>): List<RoundGroup>

}