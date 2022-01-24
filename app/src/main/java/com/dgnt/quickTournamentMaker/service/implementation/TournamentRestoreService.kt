package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IByeStatusResolverService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInitiatorService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentRestoreService

class TournamentRestoreService() : ITournamentRestoreService {
    override fun restore(tournament: Tournament, restoredTournamentInformation: RestoredTournamentInformation) {

    }
}