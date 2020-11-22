package com.dgnt.quickTournamentMaker.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.data.management.PersonRepository

class HomeViewModelFactory(private val personRepository: PersonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(personRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}