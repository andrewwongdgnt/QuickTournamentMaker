package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService

class LoadTournamentFilterOptionsViewModelFactory(
    private val preferenceService: IPreferenceService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadTournamentFilterOptionsViewModel::class.java)) {
            return LoadTournamentFilterOptionsViewModel(preferenceService) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}