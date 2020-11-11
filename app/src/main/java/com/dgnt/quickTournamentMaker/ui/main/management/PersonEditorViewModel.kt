package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import kotlinx.coroutines.launch
import java.util.*

class PersonEditorViewModel(private val personRepository: PersonRepository, private val groupRepository: GroupRepository) : ViewModel(), Observable {

    @Bindable
    val name = MutableLiveData<String>()

    @Bindable
    val note = MutableLiveData<String>()

    @Bindable
    val groupName = MutableLiveData<String>()

    @Bindable
    val newGroupName = MutableLiveData<String>()

    @Bindable
    val groupNames = MutableLiveData<List<String>>()

    private lateinit var id: String
    private lateinit var defaultGroupName: String

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


    fun setData(person: Person?, groupName: String?, groups: List<Group>?, defaultGroupName: String) {
        name.value = person?.name ?: ""
        note.value = person?.note ?: ""
        id = (person?.id ?: "").ifBlank { UUID.randomUUID().toString() }
        this.defaultGroupName = defaultGroupName
        this.groupNames.value = (groups?.map { it.name } ?: listOf()).ifEmpty { listOf(defaultGroupName) }
        this.groupName.value = (groupName ?: "")
        this.newGroupName.value = this.groupName.value
    }

    fun add() {
        insert(PersonEntity(id, name.value!!, note.value!!, newGroupName.value!!))
        this.name.value = ""
        this.note.value = ""
    }

    private fun insert(person: PersonEntity) = viewModelScope.launch {
        personRepository.insert(person)
        if (newGroupName.value == defaultGroupName)
            groupRepository.insert(GroupEntity(name = defaultGroupName, note = "", favourite = false))
    }

    fun edit() {
        edit(PersonEntity(id, name.value!!, note.value!!, newGroupName.value!!))
    }

    private fun edit(person: PersonEntity) = viewModelScope.launch {
        personRepository.update(person)
    }

    fun setNewGroupName(selectedValue: Any) {
        newGroupName.value = selectedValue as String
    }

}