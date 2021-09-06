package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.model.tournament.Tournament

interface ITournamentInitiatorService {

    /**
     * Initiate the state of the tournament
     *
     * @param tournament the tournament
     */
    fun initiate(tournament: Tournament)
}