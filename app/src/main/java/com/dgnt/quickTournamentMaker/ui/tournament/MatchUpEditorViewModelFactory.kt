package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.service.interfaces.ICreateDefaultTitleService

class MatchUpEditorViewModelFactory(private val createDefaultTitleService: ICreateDefaultTitleService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchUpEditorViewModel::class.java)) {
            return MatchUpEditorViewModel(createDefaultTitleService) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
