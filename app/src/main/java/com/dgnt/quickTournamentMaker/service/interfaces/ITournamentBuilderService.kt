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
    fun build(tournamentInfo: TournamentInformation, orderedParticipants: List<Participant>, defaultRoundGroupTitleFunc: (Int) -> String = { rgIndex -> rgIndex.toString() }, defaultRoundTitleFunc: (Int) -> String = { rIndex -> rIndex.toString() }, defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String = { mIndex,p1,p2 -> "$mIndex, $p1, $p2" }): Tournament
}