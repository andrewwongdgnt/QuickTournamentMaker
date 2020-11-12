package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.util.Event
import kotlinx.coroutines.launch

class GroupDeleteViewModel(private val personRepository: PersonRepository, private val groupRepository: GroupRepository) : ViewModel(), Observable {


    private val _messageEvent = MutableLiveData<Event<String>>()
    val messageEvent: LiveData<Event<String>>
        get() = _messageEvent


    @Bindable
    val groupName = MutableLiveData<String>()

    @Bindable
    val newGroupName = MutableLiveData<String>()

    @Bindable
    val groupNames = MutableLiveData<List<String>>()


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


    fun setData(groups: List<Group>?) {
        val groupNames = (groups?.map { it.name } ?: listOf())
        this.groupNames.value = groupNames
        this.groupName.value = groupNames.ifEmpty { listOf("") }[0]
        this.newGroupName.value = this.groupName.value
    }

    fun setNewGroupName(selectedValue: Any) {
        newGroupName.value = selectedValue as String
    }

    fun deleteThenMove(selectedGroups: List<GroupEntity>,successMsg: String) = viewModelScope.launch {
        groupRepository.delete(selectedGroups)
        personRepository.updateGroup(selectedGroups.map { it.name }, newGroupName.value!!)
        _messageEvent.value = Event(successMsg)
    }

    fun deleteWithPlayers(selectedGroups: List<GroupEntity>,successMsg: String) = viewModelScope.launch {
        groupRepository.delete(selectedGroups)
        personRepository.deleteViaGroup(selectedGroups.map { it.name })
        _messageEvent.value = Event(successMsg)
    }

}