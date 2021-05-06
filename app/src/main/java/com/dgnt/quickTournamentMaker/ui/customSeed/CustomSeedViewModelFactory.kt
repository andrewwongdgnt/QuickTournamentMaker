package com.dgnt.quickTournamentMaker.ui.customSeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.service.implementation.CustomSeedManagementService
import com.dgnt.quickTournamentMaker.service.interfaces.ICustomSeedManagementService

class CustomSeedViewModelFactory(private val customSeedManagementService: ICustomSeedManagementService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomSeedViewModel::class.java)) {
            return CustomSeedViewModel(customSeedManagementService) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
