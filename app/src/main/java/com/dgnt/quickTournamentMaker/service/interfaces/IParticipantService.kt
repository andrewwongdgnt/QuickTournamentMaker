package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
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
    fun createRound(participants: List<Participant>, roundGroupIndex: Int = 0, roundIndex: Int = 0, defaultRoundTitleFunc: (Round) -> String = { r -> r.roundIndex.toString() }, defaultMatchUpTitleFunc: (MatchUp) -> String = { m -> m.matchUpIndex.toString() }): Round

}