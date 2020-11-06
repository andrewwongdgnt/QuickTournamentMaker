package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.Event
import kotlinx.coroutines.launch

class PersonEditorViewModel(private val personRepository: PersonRepository, private val groupRepository: GroupRepository) : ViewModel(), Observable {

    val persons = personRepository.getAll()

    @Bindable
    val name = MutableLiveData<String>()

    @Bindable
    val note = MutableLiveData<String>()

    @Bindable
    val groupName = MutableLiveData<String>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(person: Person?, groupName: String?) {

        if (person != null) {
            name.value = person.name
            note.value = person.note
        }
        if (groupName != null) {
            this.groupName.value = groupName
        }
    }

    fun add() {
        val name = name.value!!
        val note = note.value!!
        val groupName = groupName.value!!
        insert(PersonEntity(name, note, groupName), GroupEntity(groupName,"",false))
        this.name.value=""
        this.note.value=""
        this.groupName.value=""
    }

    private fun insert(person: PersonEntity, group: GroupEntity) = viewModelScope.launch {
        personRepository.insert(person)
        groupRepository.insert(group)
    }




}