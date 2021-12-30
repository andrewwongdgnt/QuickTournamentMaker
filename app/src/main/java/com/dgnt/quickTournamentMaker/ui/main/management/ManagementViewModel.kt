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
    val expandAll = MutableLiveData<Boolean>()

    val messageEvent = MutableLiveData<Event<String>>()

    private val persons = personRepository.getAll()
    private val groups = groupRepository.getAll()

    val personAndGroupLiveData: LiveData<Pair<List<PersonEntity>, List<GroupEntity>>> =
        object : MediatorLiveData<Pair<List<PersonEntity>, List<GroupEntity>>>() {
            var person: List<PersonEntity>? = null
            var group: List<GroupEntity>? = null

            init {
                addSource(persons) { persons ->
                    this.person = persons
                    group?.let { value = persons to it }
                }
                addSource(groups) { groups ->
                    this.group = groups
                    person?.let { value = it to groups }
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