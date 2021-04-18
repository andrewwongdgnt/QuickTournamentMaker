package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup

interface IRoundGeneratorService {

    /**
     * build tournament brackets
     *
     * @param orderedParticipants participants in the order they were defined
     * @param defaultRoundGroupTitleFunc the function that produces a default title for round groups
     * @param defaultRoundTitleFunc the function that produces a default title for rounds
     * @param defaultMatchUpTitleFunc the function that produces a default title for match ups
     * @return List of round groups
     */
    fun build(orderedParticipants: List<Participant>, defaultRoundGroupTitleFunc: (RoundGroup) -> String = { rg -> rg.roundGroupIndex.toString() }, defaultRoundTitleFunc: (Round) -> String = { r -> r.roundIndex.toString() }, defaultMatchUpTitleFunc: (MatchUp) -> String = { m -> m.matchUpIndex.toString() }): List<RoundGroup>

}