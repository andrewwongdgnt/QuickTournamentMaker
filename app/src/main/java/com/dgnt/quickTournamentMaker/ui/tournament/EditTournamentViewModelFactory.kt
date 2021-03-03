package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditTournamentViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTournamentViewModel::class.java)) {
            return EditTournamentViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
