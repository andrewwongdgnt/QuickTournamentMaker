package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant

data class MatchUpInformation(val matchUp:MatchUp,val isParticipant1Highlighted:Boolean?)
interface ICustomSeedManagementService {

    /**
     * set up the seeding for easy customization
     *
     * @param orderedParticipants participants after they have been seeded
     * @return list of match ups
     */
    fun setUp(orderedParticipants: List<Participant>): List<MatchUp>

    /**
     * a method to make a selection.
     *
     * @param matchUpIndex the match up index
     * @param isParticipant1Selected the participant to select. So far we only have 2 participants to select from.
     * @return location of match ups to and participants to highlight or unhighlight
     */
    fun select(matchUpIndex: Int, isParticipant1Selected: Boolean): Pair<MatchUpInformation,MatchUpInformation>
}