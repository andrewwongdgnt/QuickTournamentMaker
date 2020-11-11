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

    private val _messageEvent = MutableLiveData<Event<String>>()
    val messageEvent: LiveData<Event<String>>
        get() = _messageEvent

    @Bindable
    val name = MutableLiveData<String>()

    @Bindable
    val note = MutableLiveData<String>()

    private lateinit var id: String

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


    fun setData(group: Group?) {
        name.value = group?.name ?: ""
        note.value = group?.note ?: ""
        id = (group?.id ?: "").ifBlank { UUID.randomUUID().toString() }
    }

    fun add(successMsg: String, failMsg: String) {
        insert(GroupEntity(id, name.value!!, note.value!!, false), successMsg, failMsg)
        this.name.value = ""
        this.note.value = ""
    }

    private fun insert(group: GroupEntity, successMsg: String, failMsg: String) = viewModelScope.launch {
        val groupResult = groupRepository.insert(group)
        when {
            groupResult.isEmpty() || groupResult[0] == -1L -> _messageEvent.value = Event(failMsg)
            else -> _messageEvent.value = Event(successMsg)
        }
    }

    fun edit(successMsg: String, failMsg: String) {
        edit(GroupEntity(id, name.value!!, note.value!!, false), successMsg, failMsg)
    }

    private fun edit(group: GroupEntity, successMsg: String, failMsg: String) = viewModelScope.launch {
        personRepository.updateGroup(id, group.name)
        when (groupRepository.update(group)) {
            0 -> _messageEvent.value = Event(failMsg)
            else -> _messageEvent.value = Event(successMsg)
        }
    }

}