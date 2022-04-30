package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService

class RebuildTournamentViewModelFactory(
    private val preferenceService: IPreferenceService,
    private val tournamentInformationCreatorService: ITournamentInformationCreatorService,
    private val seedServices: Map<TournamentType, ISeedService>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RebuildTournamentViewModel::class.java)) {
            return RebuildTournamentViewModel(preferenceService, tournamentInformationCreatorService, seedServices) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
