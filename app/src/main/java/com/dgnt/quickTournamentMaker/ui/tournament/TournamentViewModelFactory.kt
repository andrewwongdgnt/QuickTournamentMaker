package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TournamentViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TournamentViewModel::class.java)) {
            return TournamentViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
