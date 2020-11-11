package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.model.management.Group
import kotlinx.coroutines.launch

class MovePersonsViewModel(private val personRepository: PersonRepository) : ViewModel(), Observable {

    fun move(persons: List<PersonEntity>) = viewModelScope.launch {
        personRepository.update(persons)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}