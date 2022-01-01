package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.service.data.serializable.TournamentData
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentDataTransformerService

class DefaultTournamentDataTransformerService : ITournamentDataTransformerService {
    override fun transform(tournament: Tournament): TournamentData {
        val tournamentData = tournament.run {
             TournamentData(
                1,
                tournamentInformation.title,
                tournamentInformation.description,
                tournamentInformation.tournamentType,
            )
        }
        return tournamentData
    }
}