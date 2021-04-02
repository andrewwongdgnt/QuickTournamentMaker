package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MatchUpEditorViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchUpEditorViewModel::class.java)) {
            return MatchUpEditorViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
