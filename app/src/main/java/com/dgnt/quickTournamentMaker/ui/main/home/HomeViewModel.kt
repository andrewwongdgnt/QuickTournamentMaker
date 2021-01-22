package com.dgnt.quickTournamentMaker.ui.main.home

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.management.GroupEntity
import com.dgnt.quickTournamentMaker.data.management.IGroupRepository
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository
import com.dgnt.quickTournamentMaker.data.management.PersonEntity
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfigType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentGeneralEditorViewModel
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentTypeEditorViewModel

class HomeViewModel(personRepository: IPersonRepository, groupRepository: IGroupRepository, override val preferenceService: IPreferenceService) : ViewModel(), Observable, TournamentGeneralEditorViewModel, TournamentTypeEditorViewModel {

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

    @Bindable
    override val title = MutableLiveData<String>()

    @Bindable
    override val description = MutableLiveData<String>()

    @Bindable
    override val tournamentType = MutableLiveData<Int>()

    @Bindable
    override val rankConfig = MutableLiveData<Int>()

    @Bindable
    override val rankConfigHelpMsg = MutableLiveData<String>()

    @Bindable
    override val seedType = MutableLiveData<Int>()

    @Bindable
    override val showRankConfig = MutableLiveData<Boolean>()

    @Bindable
    override val showSeedType = MutableLiveData<Boolean>()

    @Bindable
    override val showPriorityContent = MutableLiveData<Boolean>()

    @Bindable
    override val showScoringContent = MutableLiveData<Boolean>()

    @Bindable
    override val winValue = MutableLiveData<Int>()

    @Bindable
    override val lossValue = MutableLiveData<Int>()

    @Bindable
    override val tieValue = MutableLiveData<Int>()

    @Bindable
    override val priorityConfig = MutableLiveData<Triple<RankPriorityConfigType, RankPriorityConfigType, RankPriorityConfigType>>()

    val scoreConfigLiveData: LiveData<Triple<Int, Int, Int>> = scoreConfigLiveDataCreator()

    @Bindable
    val quickStart = MutableLiveData<Boolean>(true)

    @Bindable
    val numberOfPlayers = MutableLiveData<String>()

    @Bindable
    val expandAll = MutableLiveData<Boolean>()

    @Bindable
    val selectAll = MutableLiveData<Boolean>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun something() {
        val g = title.value
        val g2 = tieValue.value
        print(title.value)

        //TODO remove this later
    }

    fun expandAll() {
        expandAll.value = true
    }

    fun collapseAll() {
        expandAll.value = false
    }

    fun selectAll() {
        selectAll.value = true
    }

    fun deselectAll() {
        selectAll.value = false
    }

}