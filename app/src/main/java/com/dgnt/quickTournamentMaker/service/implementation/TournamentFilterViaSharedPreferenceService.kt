package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentFilterService

class TournamentFilterViaSharedPreferenceService(private val preferenceService: IPreferenceService) : ITournamentFilterService {
    override fun update(restoredTournamentInformationList: List<RestoredTournamentInformation>) =
        TournamentType.values().map { preferenceService.isFilteredOnTournamentType(it) }.run { all { it } || none { it } }.let { allOrNone ->

            (if (!allOrNone) {
                var list = restoredTournamentInformationList.asSequence()

                TournamentType.values().forEach { type ->
                    list = list.filter { filterOn({ preferenceService.isFilteredOnTournamentType(type) }, { it.extendedTournamentInformation.basicTournamentInformation.tournamentType == type }) }
                }
                list.toList()
            } else {
                restoredTournamentInformationList
            })
                .asSequence()
                .filter { filterOn({ preferenceService.isFilteredOnMinimumParticipants() }, { it.extendedTournamentInformation.numParticipants >= preferenceService.getMinimumParticipantsToFilterOn() }) }
                .filter { filterOn({ preferenceService.isFilteredOnMaximumParticipants() }, { it.extendedTournamentInformation.numParticipants <= preferenceService.getMaximumParticipantsToFilterOn() }) }
                .filter { filterOn({ preferenceService.isFilteredOnEarliestCreatedDate() }, { it.extendedTournamentInformation.basicTournamentInformation.creationDate >= preferenceService.getEarliestCreatedDateToFilterOn() }) }
                .filter { filterOn({ preferenceService.isFilteredOnLatestCreatedDate() }, { it.extendedTournamentInformation.basicTournamentInformation.creationDate <= preferenceService.getLatestCreatedDateToFilterOn() }) }
                .filter {
                    it.extendedTournamentInformation.basicTournamentInformation.lastModifiedDate?.let { lastModifiedDate ->
                        filterOn({ preferenceService.isFilteredOnEarliestModifiedDate() }, { lastModifiedDate >= preferenceService.getEarliestModifiedDateToFilterOn() })
                    } ?: true
                }
                .filter {
                    it.extendedTournamentInformation.basicTournamentInformation.lastModifiedDate?.let { lastModifiedDate ->
                        filterOn({ preferenceService.isFilteredOnLatestModifiedDate() }, { lastModifiedDate <= preferenceService.getLatestModifiedDateToFilterOn() })
                    } ?: true
                }
                .toList()

        }

    // lambdas of () -> Boolean because we still want short circuiting to happen
    private inline fun filterOn(preferenceValue: () -> Boolean, actualValue: () -> Boolean) = preferenceValue() && actualValue() || !preferenceValue()


}