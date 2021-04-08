package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation

interface ITournamentBuilderService {

    /**
     * Builds the tournament
     *
     * @param tournamentInfo the tournament information
     * @param defaultRoundTitleFunc the function that produces a default title for rounds
     * @param defaultMatchUpTitleFunc the function that produces a default title for match ups
     * @return
     */
    fun build(tournamentInfo: TournamentInformation, defaultRoundTitleFunc: (Round) -> String = { r -> r.roundIndex.toString() }, defaultMatchUpTitleFunc: (MatchUp) -> String = { m -> m.matchUpIndex.toString() }): Tournament
}