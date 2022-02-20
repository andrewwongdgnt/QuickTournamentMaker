package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.Progress
import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.service.interfaces.IProgressCalculatorService

class ProgressCalculatorViaRoundsService : IProgressCalculatorService {
    override fun calculate(tournament: Tournament): Progress {
        return Progress(33,5) //TODO implement this
    }
}