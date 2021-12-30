package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentEventHolder
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentTypeEditorViewModel
import com.dgnt.quickTournamentMaker.util.Event

class RebuildTournamentViewModel(
    override val preferenceService: IPreferenceService,
    override val tournamentInformationCreatorService: ITournamentInformationCreatorService
) : ViewModel(), Observable, TournamentTypeEditorViewModel, TournamentEventHolder {


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

    @Bindable
    override val scoreConfigLiveData: LiveData<Triple<Int, Int, Int>> = scoreConfigLiveDataCreator()

    override val tournamentEvent = MutableLiveData<Event<Pair<TournamentInformation, List<Participant>>>>()

    override val customSeedTournamentEvent = MutableLiveData<Event<Pair<TournamentInformation, List<Participant>>>>()

    override val failedToStartTournamentMessage = MutableLiveData<Event<Boolean>>()

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

    private lateinit var oldTournamentInformation: TournamentInformation
    private lateinit var oldParticipants: List<Participant>

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(
        eliminationRadioButtonId: Int,
        doubleEliminationRadioButtonId: Int,
        roundRobinRadioButtonId: Int,
        swissRadioButtonId: Int,
        survivalRadioButtonId: Int,
        randomSeedingRadioButtonId: Int,
        customSeedingRadioButtonId: Int,
        priorityRankingRadioButtonId: Int,
        scoreRankingRadioButtonId: Int,
        oldTournamentInformation: TournamentInformation,
        orderedParticipants: List<Participant>
    ) {
        this.eliminationRadioButtonId = eliminationRadioButtonId
        this.doubleEliminationRadioButtonId = doubleEliminationRadioButtonId
        this.roundRobinRadioButtonId = roundRobinRadioButtonId
        this.swissRadioButtonId = swissRadioButtonId
        this.survivalRadioButtonId = survivalRadioButtonId
        this.randomSeedingRadioButtonId = randomSeedingRadioButtonId
        this.customSeedingRadioButtonId = customSeedingRadioButtonId
        this.priorityRankingRadioButtonId = priorityRankingRadioButtonId
        this.scoreRankingRadioButtonId = scoreRankingRadioButtonId
        this.oldTournamentInformation = oldTournamentInformation
        this.oldParticipants = orderedParticipants
    }

    fun startTournament() {

        val tournamentType = when (tournamentType.value) {
            eliminationRadioButtonId -> TournamentType.ELIMINATION
            doubleEliminationRadioButtonId -> TournamentType.DOUBLE_ELIMINATION
            roundRobinRadioButtonId -> TournamentType.ROUND_ROBIN
            swissRadioButtonId -> TournamentType.SWISS
            else -> TournamentType.SURVIVAL
        }

        val seedTypeId = seedType.value

        val seedTypeToPersist = when (seedTypeId) {
            randomSeedingRadioButtonId -> SeedType.RANDOM
            customSeedingRadioButtonId -> SeedType.CUSTOM
            else -> oldTournamentInformation.seedType
        }

        val newParticipants = when (seedTypeId) {
            randomSeedingRadioButtonId -> oldParticipants.shuffled()
            else -> oldParticipants
        }

        val isCustomSeedTournamentEvent = seedTypeId == customSeedingRadioButtonId

        val rankConfig = when (rankConfig.value) {
            priorityRankingRadioButtonId -> priorityConfig.value?.let { RankPriorityConfig(it.first, it.second, it.third) } ?: RankPriorityConfig.DEFAULT
            else -> RankScoreConfig(winValue.value?.toFloat() ?: 0f, lossValue.value?.toFloat() ?: 0f, tieValue.value?.toFloat() ?: 0f)
        }

        val tournamentInformation = TournamentInformation(
            oldTournamentInformation.title,
            oldTournamentInformation.description,
            tournamentType,
            seedTypeToPersist,
            rankConfig,
            oldTournamentInformation.creationDate,
            oldTournamentInformation.lastModifiedDate
        )


        Event(Pair(tournamentInformation, newParticipants)).also {
            if (isCustomSeedTournamentEvent)
                customSeedTournamentEvent.value = it
            else
                tournamentEvent.value = it
        }


    }

}