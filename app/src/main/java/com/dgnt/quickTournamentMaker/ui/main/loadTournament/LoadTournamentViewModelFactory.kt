package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.data.tournament.IMatchUpRepository
import com.dgnt.quickTournamentMaker.data.tournament.IParticipantRepository
import com.dgnt.quickTournamentMaker.data.tournament.IRoundRepository
import com.dgnt.quickTournamentMaker.data.tournament.ITournamentRepository
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentFilterService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentSortService

class LoadTournamentViewModelFactory(
    private val tournamentRepository: ITournamentRepository,
    private val roundRepository: IRoundRepository,
    private val matchUpRepository: IMatchUpRepository,
    private val participantRepository: IParticipantRepository,
    private val rankingConfigService: IRankingConfigService,
    private val preferenceService: IPreferenceService,
    private val tournamentFilterService: ITournamentFilterService,
    private val tournamentSortService: ITournamentSortService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadTournamentViewModel::class.java)) {
            return LoadTournamentViewModel(tournamentRepository, roundRepository, matchUpRepository, participantRepository, rankingConfigService, preferenceService, tournamentFilterService, tournamentSortService) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}