package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService
import java.util.*

class TournamentInformationCreatorService : ITournamentInformationCreatorService {
    override fun create(
        titleCandidate: String,
        alternativeTitles: Map<TournamentType, String>,
        description: String,
        participants: List<Participant>,
        tournamentType: TournamentType,
        seedType: SeedType,
        rankConfig: IRankConfig
    ): TournamentInformation {
        val title = if (titleCandidate.isNotBlank())
            titleCandidate
        else
            alternativeTitles.getValue(tournamentType)

        return TournamentInformation(title, description, participants, tournamentType, seedType, rankConfig, Calendar.getInstance().time)
    }


}