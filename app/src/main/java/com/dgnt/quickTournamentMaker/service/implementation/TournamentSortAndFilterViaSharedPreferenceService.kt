package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentSortAndFilterService

class TournamentSortAndFilterViaSharedPreferenceService(preferenceService: IPreferenceService) : ITournamentSortAndFilterService {
    override fun update(restoredTournamentInformationList: List<RestoredTournamentInformation>): List<RestoredTournamentInformation> {
        TODO("Not yet implemented")
    }
}