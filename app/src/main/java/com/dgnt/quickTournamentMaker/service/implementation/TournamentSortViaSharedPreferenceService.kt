package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.loadTournament.Sort
import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentSortService

class TournamentSortViaSharedPreferenceService(private val preferenceService: IPreferenceService) : ITournamentSortService {
    override fun applySort(restoredTournamentInformationList: List<RestoredTournamentInformation>) =
        when (preferenceService.getSort()) {
            Sort.CREATION_DATE_NEWEST -> restoredTournamentInformationList.sortedBy { it.extendedTournamentInformation.basicTournamentInformation.creationDate }
            Sort.CREATION_DATE_OLDEST -> restoredTournamentInformationList.sortedByDescending { it.extendedTournamentInformation.basicTournamentInformation.creationDate }
            Sort.LAST_MODIFIED_DATE_NEWEST -> restoredTournamentInformationList.sortedWith(compareBy(nullsLast()) { it.extendedTournamentInformation.basicTournamentInformation.lastModifiedDate })
            Sort.LAST_MODIFIED_DATE_OLDEST -> restoredTournamentInformationList.sortedWith(compareByDescending(nullsFirst()) { it.extendedTournamentInformation.basicTournamentInformation.lastModifiedDate })
            Sort.NAME -> restoredTournamentInformationList.sortedBy { it.extendedTournamentInformation.basicTournamentInformation.title }
            Sort.NAME_REVERSED -> restoredTournamentInformationList.sortedByDescending { it.extendedTournamentInformation.basicTournamentInformation.title }
            Sort.TOURNAMENT_TYPE -> restoredTournamentInformationList.sortedBy { it.extendedTournamentInformation.basicTournamentInformation.tournamentType }
            Sort.TOURNAMENT_TYPE_REVERSED -> restoredTournamentInformationList.sortedByDescending { it.extendedTournamentInformation.basicTournamentInformation.tournamentType }
        }

}