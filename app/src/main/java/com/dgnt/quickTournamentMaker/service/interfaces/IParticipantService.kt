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
     * @param defaultRoundTitleFunc the function that produces a default title for rounds
     * @param defaultMatchUpTitleFunc the function that produces a default title for match ups
     * @return the round
     */
    fun createRound(participants: List<Participant>, roundGroupIndex: Int = 0, roundIndex: Int = 0, defaultRoundTitleFunc: (Int) -> String = { rIndex -> rIndex.toString() }, defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String = { mIndex, p1, p2 -> "$mIndex, $p1, $p2" }): Round

    /**
     * takes the original participant list and clone it along with the extra bye and null participants
     *
     * @param original the original list
     * @param reference the reference which can include byes and nulls.
     * @return the new list
     */
    fun cloneList(original: List<Participant>, reference: List<Participant>): List<Participant>

}