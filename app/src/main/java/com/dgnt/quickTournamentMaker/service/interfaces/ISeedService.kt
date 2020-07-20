package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.Round
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup

interface ISeedService {

    /**
     * build tournament brackets
     *
     * @param orderedParticipants participants in the order they were defined
     * @return List of round groups
     */
    fun build(orderedParticipants: List<Participant>): List<RoundGroup>

    /**
     * Check if the list of participants works
     *
     * @param orderedParticipants participants in the order they were defined
     * @return whether this seeding works
     */
    fun seedCheck(orderedParticipants: List<Participant>): Boolean

}