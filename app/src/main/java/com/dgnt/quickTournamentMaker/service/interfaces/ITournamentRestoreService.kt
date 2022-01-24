package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.Tournament

interface ITournamentRestoreService {

    /**
     * restore the state of the tournament
     *
     * @param tournament the tournament
     * @param restoredTournamentInformation the info used to restore the state of the tournament
     */
    fun restore(tournament: Tournament, restoredTournamentInformation: RestoredTournamentInformation)
}