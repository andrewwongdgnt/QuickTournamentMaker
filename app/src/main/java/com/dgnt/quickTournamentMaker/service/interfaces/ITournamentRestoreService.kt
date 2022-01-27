package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.FoundationalTournamentEntities
import com.dgnt.quickTournamentMaker.model.tournament.Tournament

interface ITournamentRestoreService {

    /**
     * restore the state of the tournament
     *
     * @param tournament the tournament
     * @param foundationalTournamentEntities the entities used to restore the state of the tournament
     */
    fun restore(tournament: Tournament, foundationalTournamentEntities: FoundationalTournamentEntities)
}