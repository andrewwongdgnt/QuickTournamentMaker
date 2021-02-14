package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.IRankConfig
import com.dgnt.quickTournamentMaker.model.tournament.SeedType
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService
import java.util.*

class DefaultTournamentInformationCreatorService : ITournamentInformationCreatorService {
    override fun create(titleCandidate: String, alternativeTitles: Map<TournamentType, String>, description: String, persons: List<String>, tournamentType: TournamentType, seedType: SeedType, rankConfig: IRankConfig): TournamentInformation {
        val title = if (titleCandidate.isNotBlank())
            titleCandidate
        else
            alternativeTitles.getValue(tournamentType)

        return TournamentInformation(title, description, persons, tournamentType, seedType, rankConfig, Calendar.getInstance().time)
    }


}