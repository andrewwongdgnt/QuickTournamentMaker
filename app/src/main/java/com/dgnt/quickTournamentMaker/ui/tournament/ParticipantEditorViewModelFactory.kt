package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ParticipantEditorViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParticipantEditorViewModel::class.java)) {
            return ParticipantEditorViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
