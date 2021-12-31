package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoreInfoViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoreInfoViewModel::class.java)) {
            return MoreInfoViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
