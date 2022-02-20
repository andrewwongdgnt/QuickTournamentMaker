package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.Progress
import com.dgnt.quickTournamentMaker.model.tournament.Tournament

interface IProgressCalculatorService {

    /**
     * calculate the progress of the tournament
     *
     * @param tournament the tournament
     * @return The progress
     */
    fun calculate(tournament: Tournament): Progress
}