package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService
import org.joda.time.LocalDateTime


class TournamentInformationCreatorService : ITournamentInformationCreatorService {
    override fun create(
        titleCandidate: String,
        alternativeTitles: Map<TournamentType, String>,
        description: String,
        tournamentType: TournamentType,
        seedType: SeedType,
        rankConfig: IRankConfig
    ): TournamentInformation {
        val title = if (titleCandidate.isNotBlank())
            titleCandidate
        else
            alternativeTitles.getValue(tournamentType)

        return TournamentInformation(title, description, tournamentType, seedType, rankConfig, LocalDateTime.now())
    }


}