package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.*
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.util.Event
import kotlinx.coroutines.launch

class GroupDeleteViewModel(private val personRepository: IPersonRepository, private val groupRepository: IGroupRepository) : ViewModel(), Observable {

    val messageEvent = MutableLiveData<Event<String>>()

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
        messageEvent.value = Event(successMsg)
    }

    fun deleteWithPersons(selectedGroups: List<GroupEntity>, successMsg: String) = viewModelScope.launch {
        groupRepository.delete(selectedGroups)
        personRepository.deleteViaGroup(selectedGroups.map { it.name })
        messageEvent.value = Event(successMsg)
    }

}