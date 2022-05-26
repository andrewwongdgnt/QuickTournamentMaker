package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.IGroupRepository
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
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
    val group = MutableLiveData<Group>()

    @Bindable
    val newGroup = MutableLiveData<Group>()

    @Bindable
    val groups = MutableLiveData<List<Group>>()

    var personId: String = UUID.randomUUID().toString()
        private set

    private lateinit var defaultGroupName: String

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(person: Person?, group: Group?, groups: List<Group>?, defaultGroupName: String) {
        name.value = person?.name ?: ""
        note.value = person?.note ?: ""
        personId = person?.id ?: run { UUID.randomUUID().toString() }
        this.defaultGroupName = defaultGroupName
        this.groups.value = (groups ?: listOf()).ifEmpty { listOf(Group(name = defaultGroupName)) }
        (group ?: Group()).let {
            this.group.value = it
            this.newGroup.value = it
        }
    }

    fun add(successMsg: String, failMsg: String, forceOpen: Boolean, forceErase: Boolean) = insert(PersonEntity(name = name.value!!, note = note.value!!, groupName = newGroup.value!!.name), successMsg, failMsg, forceOpen, forceErase)


    private fun insert(person: PersonEntity, successMsg: String, failMsg: String, forceOpen: Boolean, forceErase: Boolean) = viewModelScope.launch {
        if (newGroup.value?.name == defaultGroupName) {
            groupRepository.insert(GroupEntity(name = defaultGroupName, note = "", favourite = false))
        }

        when (personRepository.insert(person)) {
            -1L -> resultEvent.value = Event(Triple(false, failMsg, false))
            else -> resultEvent.value = Event(Triple(!forceOpen, successMsg, forceErase))
        }
    }

    fun edit(successMsg: String, failMsg: String) = edit(PersonEntity(personId, name.value!!, note.value!!, newGroup.value!!.name), successMsg, failMsg)

    private fun edit(person: PersonEntity, successMsg: String, failMsg: String) = viewModelScope.launch {
        when (personRepository.update(person)) {
            0 -> resultEvent.value = Event(Triple(false, failMsg, false))
            else -> resultEvent.value = Event(Triple(true, successMsg, true))
        }
    }

    fun setNewGroupName(selectedValue: Any) {
        newGroup.value = selectedValue as Group
    }

}