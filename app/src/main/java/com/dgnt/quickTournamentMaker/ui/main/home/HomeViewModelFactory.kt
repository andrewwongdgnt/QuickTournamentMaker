package com.dgnt.quickTournamentMaker.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService

class HomeViewModelFactory(private val personRepository: PersonRepository, private val preferenceService: IPreferenceService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(personRepository, preferenceService) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}