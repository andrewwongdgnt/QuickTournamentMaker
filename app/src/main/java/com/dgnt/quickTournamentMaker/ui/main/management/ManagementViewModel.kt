package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.Event

class ManagementViewModel(private val personRepository: PersonRepository, private val groupRepository: GroupRepository) : ViewModel(), Observable {

    val persons = personRepository.getAll()
    val groups = groupRepository.getAll()

    private val _navigateToPersonDetails = MutableLiveData<Event<Triple<Person,String,Boolean>>>()
    val navigateToPersonDetails: LiveData<Event<Triple<Person,String,Boolean>>>
        get() = _navigateToPersonDetails

    fun addPerson() {
        _navigateToPersonDetails.value = Event(Triple(Person("",""),"",false))
    }

    fun editPerson(person: Person,groupName:String) {
        _navigateToPersonDetails.value = Event(Triple(person,groupName,true))
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}