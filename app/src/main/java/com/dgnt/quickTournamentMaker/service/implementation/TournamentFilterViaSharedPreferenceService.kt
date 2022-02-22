package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.RestoredTournamentInformation
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentFilterService

class TournamentFilterViaSharedPreferenceService(private val preferenceService: IPreferenceService) : ITournamentFilterService {
    override fun applyFilter(restoredTournamentInformationList: List<RestoredTournamentInformation>) =
        TournamentType.values().map { preferenceService.isFilteredOnTournamentType(it) }.run { none { it } }.let { none ->

            restoredTournamentInformationList
                .asSequence()
                .filter {
                    var finalBoolean = none
                    TournamentType.values().forEach { type ->
                        finalBoolean = finalBoolean || preferenceService.isFilteredOnTournamentType(type) && it.extendedTournamentInformation.basicTournamentInformation.tournamentType == type
                    }
                    finalBoolean
                }
                .filter { filterOn({ preferenceService.isFilteredOnMinimumParticipants() }, { it.extendedTournamentInformation.numParticipants >= preferenceService.getMinimumParticipantsToFilterOn() }) }
                .filter { filterOn({ preferenceService.isFilteredOnMaximumParticipants() }, { it.extendedTournamentInformation.numParticipants <= preferenceService.getMaximumParticipantsToFilterOn() }) }
                .filter { filterOn({ preferenceService.isFilteredOnEarliestCreatedDate() }, { it.extendedTournamentInformation.basicTournamentInformation.creationDate.toLocalDate() >= preferenceService.getEarliestCreatedDateToFilterOn().toLocalDate() }) }
                .filter { filterOn({ preferenceService.isFilteredOnLatestCreatedDate() }, { it.extendedTournamentInformation.basicTournamentInformation.creationDate.toLocalDate() <= preferenceService.getLatestCreatedDateToFilterOn().toLocalDate() }) }
                .filter {
                    it.extendedTournamentInformation.basicTournamentInformation.lastModifiedDate?.let { lastModifiedDate ->
                        filterOn({ preferenceService.isFilteredOnEarliestModifiedDate() }, { lastModifiedDate.toLocalDate() >= preferenceService.getEarliestModifiedDateToFilterOn().toLocalDate() })
                    } ?: true
                }
                .filter {
                    it.extendedTournamentInformation.basicTournamentInformation.lastModifiedDate?.let { lastModifiedDate ->
                        filterOn({ preferenceService.isFilteredOnLatestModifiedDate() }, { lastModifiedDate.toLocalDate() <= preferenceService.getLatestModifiedDateToFilterOn().toLocalDate() })
                    } ?: true
                }
                .filter { filterOn({ preferenceService.isFilteredOnProgress() }, { it.extendedTournamentInformation.progress.percentageRep >= preferenceService.getLeastProgressToFilterOn() && it.extendedTournamentInformation.progress.percentageRep <= preferenceService.getMostProgressToFilterOn() }) }
                .toList()

        }

    // lambdas of () -> Boolean because we still want short circuiting to happen
    private inline fun filterOn(preferenceValue: () -> Boolean, actualValue: () -> Boolean) = preferenceValue() && actualValue() || !preferenceValue()


}