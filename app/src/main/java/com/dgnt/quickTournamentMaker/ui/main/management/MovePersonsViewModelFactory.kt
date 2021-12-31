package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository

class MovePersonsViewModelFactory(
    private val personRepository: IPersonRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovePersonsViewModel::class.java)) {
            return MovePersonsViewModel(personRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}