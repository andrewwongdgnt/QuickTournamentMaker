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
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ISelectedPlayersService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentGeneralEditorViewModel
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentTypeEditorViewModel

class HomeViewModel(personRepository: IPersonRepository, groupRepository: IGroupRepository, override val preferenceService: IPreferenceService, override val tournamentInformationCreatorService: ITournamentInformationCreatorService, val selectedPlayersService: ISelectedPlayersService) : ViewModel(), Observable, TournamentGeneralEditorViewModel, TournamentTypeEditorViewModel {

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
    val numberOfPlayers = MutableLiveData<Int>()

    @Bindable
    val numberOfPlayersSelected = MutableLiveData<String>()

    @Bindable
    val selectedPlayers = MutableLiveData<List<String>>()

    @Bindable
    val expandAll = MutableLiveData<Boolean>()

    @Bindable
    val selectAll = MutableLiveData<Boolean>()

    val noPlayers: LiveData<Boolean> =
        object : MediatorLiveData<Boolean>() {
            init {
                addSource(persons) { persons ->
                    value = persons.isEmpty()
                }
            }
        }

    override lateinit var alternativeTitles: Map<TournamentType, String>
    override var eliminationRadioButtonId = 0
    override var doubleEliminationRadioButtonId = 0
    override var roundRobinRadioButtonId = 0
    override var swissRadioButtonId = 0
    override var survivalRadioButtonId = 0

    override var randomSeedingRadioButtonId = 0
    override var customSeedingRadioButtonId = 0
    override var sameSeedingRadioButtonId = 0

    override var priorityRankingRadioButtonId = 0
    override var scoreRankingRadioButtonId = 0

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(alternativeTitles: Map<TournamentType, String>, eliminationRadioButtonId: Int, doubleEliminationRadioButtonId: Int, roundRobinRadioButtonId: Int, swissRadioButtonId: Int, survivalRadioButtonId: Int, randomSeedingRadioButtonId: Int, customSeedingRadioButtonId: Int, priorityRankingRadioButtonId: Int, scoreRankingRadioButtonId: Int) {
        this.alternativeTitles = alternativeTitles
        this.eliminationRadioButtonId = eliminationRadioButtonId
        this.doubleEliminationRadioButtonId = doubleEliminationRadioButtonId
        this.roundRobinRadioButtonId = roundRobinRadioButtonId
        this.swissRadioButtonId = swissRadioButtonId
        this.survivalRadioButtonId = survivalRadioButtonId
        this.randomSeedingRadioButtonId = randomSeedingRadioButtonId
        this.customSeedingRadioButtonId = customSeedingRadioButtonId
        this.priorityRankingRadioButtonId = priorityRankingRadioButtonId
        this.scoreRankingRadioButtonId = scoreRankingRadioButtonId
    }

    fun startTournament() {

        val tournamentType = when (tournamentType.value) {
            eliminationRadioButtonId -> TournamentType.ELIMINATION
            doubleEliminationRadioButtonId -> TournamentType.DOUBLE_ELIMINATION
            roundRobinRadioButtonId -> TournamentType.ROUND_ROBIN
            swissRadioButtonId -> TournamentType.SWISS
            else -> TournamentType.SURVIVAL

        }
        val seedType = when (seedType.value) {
            randomSeedingRadioButtonId -> SeedType.RANDOM
            else -> SeedType.CUSTOM
        }

        val rankConfig = when (rankConfig.value) {
            priorityRankingRadioButtonId -> if (priorityConfig.value == null) RankPriorityConfig.DEFAULT else RankPriorityConfig(priorityConfig.value!!.first, priorityConfig.value!!.first, priorityConfig.value!!.first)
            else -> RankScoreConfig(if (winValue.value == null) 0f else winValue.value!! * 1f, if (lossValue.value == null) 0f else lossValue.value!! * 1f, if (tieValue.value == null) 0f else tieValue.value!! * 1f)
        }

        tournamentInformationCreatorService.create(title.value ?: "", alternativeTitles, description.value ?: "", selectedPlayersService.resolve(selectedPlayers.value, numberOfPlayers.value, quickStart.value ?: false, seedType), tournamentType, seedType, rankConfig)


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