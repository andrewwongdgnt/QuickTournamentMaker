package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.service.data.serializable.TournamentData

interface ITournamentDataTransformerService {

    fun transform(tournament:Tournament): TournamentData
}