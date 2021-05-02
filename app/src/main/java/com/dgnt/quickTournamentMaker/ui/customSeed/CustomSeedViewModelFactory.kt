package com.dgnt.quickTournamentMaker.ui.customSeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CustomSeedViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomSeedViewModel::class.java)) {
            return CustomSeedViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
