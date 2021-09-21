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
import com.dgnt.quickTournamentMaker.model.management.Person
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService
import com.dgnt.quickTournamentMaker.service.interfaces.ISelectedPersonsService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentGeneralEditorViewModel
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentTypeEditorViewModel
import com.dgnt.quickTournamentMaker.util.Event

class HomeViewModel(personRepository: IPersonRepository, groupRepository: IGroupRepository, override val preferenceService: IPreferenceService, override val tournamentInformationCreatorService: ITournamentInformationCreatorService, private val selectedPersonsService: ISelectedPersonsService, private val seedServices: Map<TournamentType, ISeedService>) : ViewModel(), Observable, TournamentGeneralEditorViewModel, TournamentTypeEditorViewModel {

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
    val numberOfParticipants = MutableLiveData<String>()

    @Bindable
    val numberOfPersonsSelected = MutableLiveData<String>()

    @Bindable
    val selectedPersons = MutableLiveData<List<Person>>()

    @Bindable
    val expandAll = MutableLiveData<Boolean>()

    @Bindable
    val selectAll = MutableLiveData<Boolean>()

    val noPersons: LiveData<Boolean> =
        object : MediatorLiveData<Boolean>() {
            init {
                addSource(persons) { persons ->
                    value = persons.isEmpty()
                }
            }
        }

    private val _randomSeedTournamentEvent = MutableLiveData<Event<Pair<TournamentInformation, List<Participant>>>>()
    val randomSeedTournamentEvent: LiveData<Event<Pair<TournamentInformation, List<Participant>>>>
        get() = _randomSeedTournamentEvent

    private val _customSeedTournamentEvent = MutableLiveData<Event<Pair<TournamentInformation, List<Participant>>>>()
    val customSeedTournamentEvent: LiveData<Event<Pair<TournamentInformation, List<Participant>>>>
        get() = _customSeedTournamentEvent

    private val _failedToStartTournamentMessage = MutableLiveData<Event<Boolean>>()
    val failedToStartTournamentMessage: LiveData<Event<Boolean>>
        get() = _failedToStartTournamentMessage

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

    private lateinit var defaultParticipantNameFunc: (Int) -> String

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(alternativeTitles: Map<TournamentType, String>, eliminationRadioButtonId: Int, doubleEliminationRadioButtonId: Int, roundRobinRadioButtonId: Int, swissRadioButtonId: Int, survivalRadioButtonId: Int, randomSeedingRadioButtonId: Int, customSeedingRadioButtonId: Int, priorityRankingRadioButtonId: Int, scoreRankingRadioButtonId: Int, defaultParticipantNameFunc: (Int) -> String) {
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
        this.defaultParticipantNameFunc = defaultParticipantNameFunc
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
            priorityRankingRadioButtonId -> priorityConfig.value?.let { RankPriorityConfig(it.first, it.second, it.third) } ?: RankPriorityConfig.DEFAULT
            else -> RankScoreConfig(winValue.value?.let { it.toFloat() } ?: 0f, lossValue.value?.let { it.toFloat() } ?: 0f, tieValue.value?.let { it.toFloat() } ?: 0f)
        }

        try {
            val tournamentInformation = tournamentInformationCreatorService.create(title.value ?: "", alternativeTitles, description.value ?: "", selectedPersonsService.resolve(selectedPersons.value, numberOfParticipants.value?.toIntOrNull(), quickStart.value ?: false, seedType, defaultParticipantNameFunc), tournamentType, seedType, rankConfig)
            val orderedParticipants = seedServices.getValue(tournamentInformation.tournamentType).seed(tournamentInformation.participants)
            Event(Pair(tournamentInformation, orderedParticipants)).also {
                if (seedType == SeedType.RANDOM)
                    _randomSeedTournamentEvent.value = it
                else if (seedType == SeedType.CUSTOM)
                    _customSeedTournamentEvent.value = it
            }


        } catch (e: IllegalArgumentException) {
            _failedToStartTournamentMessage.value = Event(true)
        }


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