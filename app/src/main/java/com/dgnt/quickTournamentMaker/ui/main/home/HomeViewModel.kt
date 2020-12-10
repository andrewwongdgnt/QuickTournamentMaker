package com.dgnt.quickTournamentMaker.ui.main.home

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.management.IPersonRepository
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentGeneralEditorViewModel
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentTypeEditorViewModel

class HomeViewModel(personRepository: IPersonRepository, private val preferenceService: IPreferenceService) : ViewModel(), Observable, TournamentGeneralEditorViewModel, TournamentTypeEditorViewModel {

    private val persons = personRepository.getAll()

    @Bindable
    override val title = MutableLiveData<String>()

    @Bindable
    override val description = MutableLiveData<String>()

    @Bindable
    override val tournamentType = MutableLiveData<Int>()

    @Bindable
    override val rankConfig = MutableLiveData<Int>()

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
    override val numberOfPlayers = MutableLiveData<Int>()

    @Bindable
    override val winValue = MutableLiveData<Int>()

    @Bindable
    override val lossValue = MutableLiveData<Int>()

    @Bindable
    override val tieValue = MutableLiveData<Int>()

    val scoreConfigLiveData: LiveData<Triple<Int, Int, Int>> =
        object : MediatorLiveData<Triple<Int, Int, Int>>() {
            var win: Int? = null
            var loss: Int? = null
            var tie: Int? = null

            init {
                addSource(winValue) {
                    win = it
                    value = Triple(win ?: 0, loss ?: 0, tie ?: 0)
                }
                addSource(lossValue) {
                    loss = it
                    value = Triple(win ?: 0, loss ?: 0, tie ?: 0)
                }
                addSource(tieValue) {
                    tie = it
                    value = Triple(win ?: 0, loss ?: 0, tie ?: 0)
                }
            }
        }

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

    fun handleTournamentTypeChange(radioButtonId: Int, roundRobinRadioButtonId: Int, swissRadioButtonId: Int, compareRankFromPriorityRadioButtonId: Int, compareRankFromScoreRadioButton: Int) {
        val isRankingBasedOnPriority = when (radioButtonId) {
            roundRobinRadioButtonId -> preferenceService.isRankingBasedOnPriority(TournamentType.ROUND_ROBIN)
            swissRadioButtonId -> preferenceService.isRankingBasedOnPriority(TournamentType.SWISS)
            else -> null
        }

        if (isRankingBasedOnPriority != null)
            rankConfig.value = if (isRankingBasedOnPriority) compareRankFromPriorityRadioButtonId else compareRankFromScoreRadioButton

    }

    fun handleRankConfigChange(value: Boolean, roundRobinRadioButtonId: Int, swissRadioButtonId: Int) {
        val tournamentType = when (tournamentType.value) {

            roundRobinRadioButtonId -> TournamentType.ROUND_ROBIN
            swissRadioButtonId -> TournamentType.SWISS
            else -> null
        }

        if (tournamentType != null) {
            preferenceService.setRankingBasedOnPriority(tournamentType, value)
            if (!value) {
                val rankScoreConfig = preferenceService.getRankScore(tournamentType)
                winValue.value = (rankScoreConfig.win * 2).toInt()
                lossValue.value = (rankScoreConfig.loss * 2).toInt()
                tieValue.value = (rankScoreConfig.tie * 2).toInt()
            }
        }
    }


    fun handleScoreConfigChange(win: Int, loss: Int, tie: Int, roundRobinRadioButtonId: Int, swissRadioButtonId: Int) {

        val tournamentType = when (tournamentType.value) {

            roundRobinRadioButtonId -> TournamentType.ROUND_ROBIN
            swissRadioButtonId -> TournamentType.SWISS
            else -> null
        }

        if (tournamentType != null)
            preferenceService.setRankScore(tournamentType, RankScoreConfig(win * 0.5f, loss * 0.5f, tie * 0.5f))
    }


}