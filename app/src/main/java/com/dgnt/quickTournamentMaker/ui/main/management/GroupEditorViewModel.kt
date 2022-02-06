package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.IGroupRepository
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository
import com.dgnt.quickTournamentMaker.model.management.Group
import com.dgnt.quickTournamentMaker.util.Event
import kotlinx.coroutines.launch
import java.util.*

class GroupEditorViewModel(private val personRepository: IPersonRepository, private val groupRepository: IGroupRepository) : ViewModel(), Observable {

    //First = dismiss or not the dialog
    //Second = resulting message
    //Third = reset fields or not
    val resultEvent = MutableLiveData<Event<Triple<Boolean, String, Boolean>>>()

    @Bindable
    val name = MutableLiveData<String>()

    @Bindable
    val note = MutableLiveData<String>()

    private lateinit var id: String
    private lateinit var oldGroupName: String

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


    fun setData(group: Group) {
        oldGroupName = group.name
        name.value = group.name
        note.value = group.note
        id = (group.id).ifBlank { UUID.randomUUID().toString() }
    }

    fun add(successMsg: String, failMsg: String, forceOpen: Boolean, forceErase: Boolean) = insert(GroupEntity(name = name.value!!, note = note.value!!, favourite = false), successMsg, failMsg, forceOpen, forceErase)

    private fun insert(group: GroupEntity, successMsg: String, failMsg: String, forceOpen: Boolean, forceErase: Boolean) = viewModelScope.launch {
        when (groupRepository.insert(group)) {
            -1L -> resultEvent.value = Event(Triple(false, failMsg, false))
            else -> resultEvent.value = Event(Triple(!forceOpen, successMsg, forceErase))
        }
    }

    fun edit(successMsg: String, failMsg: String) {
        edit(GroupEntity(id, name.value!!, note.value!!, false), oldGroupName, successMsg, failMsg)
    }

    private fun edit(group: GroupEntity, oldGroupName: String, successMsg: String, failMsg: String) = viewModelScope.launch {
        when (groupRepository.update(group)) {
            0 -> resultEvent.value = Event(Triple(false, failMsg, false))
            else -> {
                resultEvent.value = Event(Triple(true, successMsg, true))
                personRepository.updateGroup(listOf(oldGroupName), group.name)
            }
        }

    }

}