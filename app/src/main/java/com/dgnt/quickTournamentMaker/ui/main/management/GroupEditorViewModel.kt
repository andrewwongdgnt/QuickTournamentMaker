package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.GroupRepository
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.model.management.Group
import kotlinx.coroutines.launch
import java.util.*

class GroupEditorViewModel(private val groupRepository: GroupRepository) : ViewModel(), Observable {

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

    fun add() {
        insert(GroupEntity(id, name.value!!, note.value!!, false))
        this.name.value = ""
        this.note.value = ""
    }

    private fun insert(group: GroupEntity) = viewModelScope.launch {
        groupRepository.insert(group)
    }

    fun edit() {
        edit(GroupEntity(id, name.value!!, note.value!!, false))
    }

    private fun edit(group: GroupEntity) = viewModelScope.launch {
        groupRepository.update(group)
    }

}