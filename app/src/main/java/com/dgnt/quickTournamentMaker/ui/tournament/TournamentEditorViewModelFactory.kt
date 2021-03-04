package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TournamentEditorViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TournamentEditorViewModel::class.java)) {
            return TournamentEditorViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
