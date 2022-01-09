package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.data.tournament.*

class LoadTournamentViewModelFactory(
    private val tournamentRepository: ITournamentRepository,
    private val roundRepository: IRoundRepository,
    private val matchUpRepository: IMatchUpRepository,
    private val participantRepository: IParticipantRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadTournamentViewModel::class.java)) {
            return LoadTournamentViewModel(tournamentRepository, roundRepository, matchUpRepository, participantRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}