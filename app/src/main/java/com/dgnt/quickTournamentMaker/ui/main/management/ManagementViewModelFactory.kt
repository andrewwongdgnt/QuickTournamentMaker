package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository

class ManagementViewModelFactory(private val personRepository: PersonRepository, private val groupRepository: GroupRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManagementViewModel::class.java)) {
            return ManagementViewModel(personRepository, groupRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}