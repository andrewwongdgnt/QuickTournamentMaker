package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService

class RoundEditorViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoundEditorViewModel::class.java)) {
            return RoundEditorViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
