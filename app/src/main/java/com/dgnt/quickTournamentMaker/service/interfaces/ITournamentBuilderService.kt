package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.*

interface ITournamentBuilderService {

    /**
     * Builds the tournament
     *
     * @param tournamentInfo the tournament information
     * @param orderedParticipants participants after they have been seeded
     * @param defaultRoundGroupTitleFunc the function that produces a default title for round groups
     * @param defaultRoundTitleFunc the function that produces a default title for rounds
     * @param defaultMatchUpTitleFunc the function that produces a default title for match ups
     * @return
     */
    fun build(tournamentInfo: TournamentInformation, orderedParticipants: List<Participant>, defaultRoundGroupTitleFunc: (RoundGroup) -> String = { rg -> rg.roundGroupIndex.toString() }, defaultRoundTitleFunc: (Round) -> String = { r -> r.roundIndex.toString() }, defaultMatchUpTitleFunc: (MatchUp) -> String = { m -> m.matchUpIndex.toString() }): Tournament
}