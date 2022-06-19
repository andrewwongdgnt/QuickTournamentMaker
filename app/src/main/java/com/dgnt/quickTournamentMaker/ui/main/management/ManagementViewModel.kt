package com.dgnt.quickTournamentMaker.ui.main.management

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.IGroupRepository
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.util.Event
import kotlinx.coroutines.launch

class ManagementViewModel(private val personRepository: IPersonRepository, groupRepository: IGroupRepository) : ViewModel(), Observable {


    @Bindable
    val expandAll = MutableLiveData(true)
    val groupsExpanded = mutableSetOf<String>()

    val messageEvent = MutableLiveData<Event<String>>()

    private val persons = personRepository.getAll()
    private val groups = groupRepository.getAll()

    val personAndGroupLiveData: LiveData<Pair<List<PersonEntity>, List<GroupEntity>>> =
        object : MediatorLiveData<Pair<List<PersonEntity>, List<GroupEntity>>>() {
            private var personEntities: List<PersonEntity>? = null
            private var groupEntities: List<GroupEntity>? = null

            init {
                addSource(persons) { persons ->
                    this.personEntities = persons
                    groupEntities?.let { value = persons to it }
                }
                addSource(groups) { groups ->
                    this.groupEntities = groups
                    personEntities?.let { value = it to groups }
                }
            }
        }


    fun delete(persons: List<PersonEntity>, successMsg: String) = viewModelScope.launch {
        personRepository.delete(persons)
        messageEvent.value = Event(successMsg)
    }

    fun expandAll() {
        expandAll.value = true
    }

    fun collapseAll() {
        expandAll.value = false
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}