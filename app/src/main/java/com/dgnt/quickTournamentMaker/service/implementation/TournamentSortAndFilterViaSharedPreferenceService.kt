package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentSortAndFilterService

class TournamentSortAndFilterViaSharedPreferenceService(private val preferenceService: IPreferenceService) : ITournamentSortAndFilterService {
    override fun update(restoredTournamentInformationList: List<RestoredTournamentInformation>) =
        TournamentType.values().map { preferenceService.isFilteredOnTournamentType(it) }.run { all { it } || none { it } }.let { allOrNone ->

            if (!allOrNone) {

                var list = restoredTournamentInformationList

                TournamentType.values().forEach { type ->
                    list = list.filter { (preferenceService.isFilteredOnTournamentType(type) && it.extendedTournamentInformation.basicTournamentInformation.tournamentType == type) || !preferenceService.isFilteredOnTournamentType(type) }
                }
                list
            } else {
                restoredTournamentInformationList
            }.let { finalList ->
                finalList
                    .filter { preferenceService.isFilteredOnMinimumParticipants() && it.extendedTournamentInformation.numParticipants >= preferenceService.getMinimumParticipantsToFilterOn() || !preferenceService.isFilteredOnMinimumParticipants() }
                    .filter { preferenceService.isFilteredOnMaximumParticipants() && it.extendedTournamentInformation.numParticipants <= preferenceService.getMaximumParticipantsToFilterOn() || !preferenceService.isFilteredOnMaximumParticipants() }
            }

        }


}