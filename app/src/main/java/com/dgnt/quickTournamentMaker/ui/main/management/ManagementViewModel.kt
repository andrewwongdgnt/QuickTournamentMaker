package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository

class ManagementViewModel(private val personRepository: PersonRepository, private val groupRepository: GroupRepository) : ViewModel() , Observable{

    fun addPerson(){
        //TODO add code
        print("FAB clicked")
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}