package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.IGroupRepository
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository

class GroupDeleteViewModelFactory(private val personRepository: IPersonRepository, private val groupRepository: IGroupRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroupDeleteViewModel::class.java)) {
            return GroupDeleteViewModel(personRepository, groupRepository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}