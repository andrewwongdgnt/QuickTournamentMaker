package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.Event

class ManagementViewModel(private val personRepository: PersonRepository, private val groupRepository: GroupRepository) : ViewModel() , Observable{

    private val _navigateToPersonDetails = MutableLiveData<Event<String>>()
    val navigateToPersonDetails : LiveData<Event<String>>
        get() = _navigateToPersonDetails

    fun addPerson(){
        //TODO
        _navigateToPersonDetails.value = Event("wtf")

    }

    fun editPerson(person: Person){
        _navigateToPersonDetails.value = Event(person.name)

    }

    fun editPe(name: String){
        _navigateToPersonDetails.value = Event(name)

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}