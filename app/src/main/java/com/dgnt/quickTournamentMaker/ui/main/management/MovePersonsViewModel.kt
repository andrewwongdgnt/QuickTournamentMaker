package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.util.Event
import kotlinx.coroutines.launch

class MovePersonsViewModel(private val personRepository: IPersonRepository) : ViewModel(), Observable {

    val messageEvent= MutableLiveData<Event<String>>()

    fun move(persons: List<PersonEntity>, successMsg: String, failMsg: String) = viewModelScope.launch {
        val results = personRepository.update(persons)
        when {
            persons.size > results -> messageEvent.value = Event(failMsg)
            else -> messageEvent.value = Event(successMsg)
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}