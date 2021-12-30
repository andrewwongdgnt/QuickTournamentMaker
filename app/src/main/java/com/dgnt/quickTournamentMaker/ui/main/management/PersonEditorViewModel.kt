package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.*
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.util.Event
import kotlinx.coroutines.launch
import java.util.*

class PersonEditorViewModel(private val personRepository: IPersonRepository, private val groupRepository: IGroupRepository) : ViewModel(), Observable {

    //First = dismiss or not the dialog
    //Second = resulting message
    //Third = reset fields or not
    val resultEvent = MutableLiveData<Event<Triple<Boolean, String, Boolean>>>()


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

    fun add(successMsg: String, failMsg: String, forceOpen: Boolean, forceErase: Boolean) = insert(PersonEntity(name = name.value!!, note = note.value!!, groupName = newGroupName.value!!), successMsg, failMsg, forceOpen, forceErase)


    private fun insert(person: PersonEntity, successMsg: String, failMsg: String, forceOpen: Boolean, forceErase: Boolean) = viewModelScope.launch {
        if (newGroupName.value == defaultGroupName) {
            groupRepository.insert(GroupEntity(name = defaultGroupName, note = "", favourite = false))
        }

        val personResult = personRepository.insert(person)
        when {
            personResult.isEmpty() || personResult[0] == -1L -> resultEvent.value = Event(Triple(false, failMsg, false))
            else -> resultEvent.value = Event(Triple(!forceOpen, successMsg, forceErase))
        }


    }

    fun edit(successMsg: String, failMsg: String) = edit(PersonEntity(id, name.value!!, note.value!!, newGroupName.value!!), successMsg, failMsg)


    private fun edit(person: PersonEntity, successMsg: String, failMsg: String) = viewModelScope.launch {
        when (personRepository.update(person)) {
            0 -> resultEvent.value = Event(Triple(false, failMsg, false))
            else -> resultEvent.value = Event(Triple(true, successMsg, true))
        }
    }

    fun setNewGroupName(selectedValue: Any) {
        newGroupName.value = selectedValue as String
    }

}