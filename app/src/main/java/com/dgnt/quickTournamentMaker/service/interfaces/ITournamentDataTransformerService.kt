package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.service.data.serializable.TournamentData

interface ITournamentDataTransformerService {

    /**
     * Transforms a tournament in a data class for export
     *
     * @param tournament the tournament
     * @return the data class for exporting tournament
     */
    fun transform(tournament:Tournament): TournamentData
}