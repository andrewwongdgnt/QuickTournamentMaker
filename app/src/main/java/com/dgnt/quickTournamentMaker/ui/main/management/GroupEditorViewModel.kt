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
import java.util.*

class GroupEditorViewModel(private val personRepository: PersonRepository, private val groupRepository: GroupRepository) : ViewModel(), Observable {

    //First = dismiss or not the dialog
    //Second = resulting message
    //Third = reset fields or not
    private val _resultEvent = MutableLiveData<Event<Triple<Boolean, String, Boolean>>>()
    val resultEvent: LiveData<Event<Triple<Boolean, String, Boolean>>>
        get() = _resultEvent

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


    fun setData(group: Group?) {
        oldGroupName = group?.name ?: ""
        name.value = group?.name ?: ""
        note.value = group?.note ?: ""
        id = (group?.id ?: "").ifBlank { UUID.randomUUID().toString() }
    }

    fun add(successMsg: String, failMsg: String, forceOpen: Boolean, forceErase: Boolean) = insert(GroupEntity(id, name.value!!, note.value!!, false), successMsg, failMsg, forceOpen, forceErase)

    private fun insert(group: GroupEntity, successMsg: String, failMsg: String, forceOpen: Boolean, forceErase: Boolean) = viewModelScope.launch {
        val groupResult = groupRepository.insert(group)
        when {
            groupResult.isEmpty() || groupResult[0] == -1L -> _resultEvent.value = Event(Triple(false, failMsg, false))
            else -> _resultEvent.value = Event(Triple(!forceOpen, successMsg, forceErase))
        }
    }

    fun edit(successMsg: String, failMsg: String) {
        edit(GroupEntity(id, name.value!!, note.value!!, false), oldGroupName, successMsg, failMsg)
    }

    private fun edit(group: GroupEntity, oldGroupName: String, successMsg: String, failMsg: String) = viewModelScope.launch {
        when (groupRepository.update(group)) {
            0 -> _resultEvent.value = Event(Triple(false, failMsg, false))
            else -> {
                _resultEvent.value = Event(Triple(true, successMsg, true))
                personRepository.updateGroup(listOf(oldGroupName), group.name)
            }
        }

    }

}