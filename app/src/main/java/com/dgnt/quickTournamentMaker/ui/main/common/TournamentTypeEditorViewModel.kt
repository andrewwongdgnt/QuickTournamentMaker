package com.dgnt.quickTournamentMaker.ui.main.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfigType
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInformationCreatorService

interface TournamentTypeEditorViewModel {

    val preferenceService: IPreferenceService
    val tournamentInformationCreatorService: ITournamentInformationCreatorService

    val tournamentType: MutableLiveData<Int>
    val rankConfig: MutableLiveData<Int>
    val rankConfigHelpMsg: MutableLiveData<String>
    val seedType: MutableLiveData<Int>
    val showRankConfig: MutableLiveData<Boolean>
    val showSeedType: MutableLiveData<Boolean>
    val showPriorityContent: MutableLiveData<Boolean>
    val showScoringContent: MutableLiveData<Boolean>
    val winValue: MutableLiveData<Int>
    val lossValue: MutableLiveData<Int>
    val tieValue: MutableLiveData<Int>
    val priorityConfig: MutableLiveData<Triple<RankPriorityConfigType, RankPriorityConfigType, RankPriorityConfigType>>
    val scoreConfigLiveData: LiveData<Triple<Int, Int, Int>>

    var eliminationRadioButtonId: Int
    var doubleEliminationRadioButtonId: Int
    var roundRobinRadioButtonId: Int
    var swissRadioButtonId: Int
    var survivalRadioButtonId: Int

    var randomSeedingRadioButtonId: Int
    var customSeedingRadioButtonId: Int
    var sameSeedingRadioButtonId: Int

    var priorityRankingRadioButtonId: Int
    var scoreRankingRadioButtonId: Int

    fun scoreConfigLiveDataCreator() =
        object : MediatorLiveData<Triple<Int, Int, Int>>() {
            var win: Int? = null
            var loss: Int? = null
            var tie: Int? = null

            init {

                val getTrip: () -> Triple<Int, Int, Int> = {
                    Triple(win ?: 0, loss ?: 0, tie ?: 0)
                }
                addSource(winValue) {
                    win = it
                    value = getTrip()
                }
                addSource(lossValue) {
                    loss = it
                    value = getTrip()
                }
                addSource(tieValue) {
                    tie = it
                    value = getTrip()
                }
            }
        }


    fun handleTournamentTypeChange(radioButtonId: Int) {
        val isRankingBasedOnPriority = when (radioButtonId) {
            roundRobinRadioButtonId -> preferenceService.isRankingBasedOnPriority(TournamentType.ROUND_ROBIN)
            swissRadioButtonId -> preferenceService.isRankingBasedOnPriority(TournamentType.SWISS)
            else -> null
        }

        if (isRankingBasedOnPriority != null)
            rankConfig.value = if (isRankingBasedOnPriority) priorityRankingRadioButtonId else scoreRankingRadioButtonId

    }

    fun handleRankConfigHelpMsgChange(radioButtonId: Int, rankConfigurationForRoundRobinHelpMsg: String, rankConfigurationForSwissHelpMsg: String) {
        if (radioButtonId == roundRobinRadioButtonId) {
            rankConfigHelpMsg.value = rankConfigurationForRoundRobinHelpMsg
        } else if (radioButtonId == swissRadioButtonId) {
            rankConfigHelpMsg.value = rankConfigurationForSwissHelpMsg
        }
    }


    fun handleRankConfigChange(value: Boolean) {
        val tournamentType = when (tournamentType.value) {

            roundRobinRadioButtonId -> TournamentType.ROUND_ROBIN
            swissRadioButtonId -> TournamentType.SWISS
            else -> null
        }

        if (tournamentType != null) {
            preferenceService.setRankingBasedOnPriority(tournamentType, value)
            if (value) {
                priorityConfig.value = preferenceService.getRankPriority(tournamentType).tripleRepresentation
            } else {
                val rankScoreConfig = preferenceService.getRankScore(tournamentType)
                winValue.value = (rankScoreConfig.win * 2).toInt()
                lossValue.value = (rankScoreConfig.loss * 2).toInt()
                tieValue.value = (rankScoreConfig.tie * 2).toInt()
            }
        }
    }


    fun handlePriorityConfigChange(priorityList: List<RankPriorityConfigType>) {

        val tournamentType = when (tournamentType.value) {

            roundRobinRadioButtonId -> TournamentType.ROUND_ROBIN
            swissRadioButtonId -> TournamentType.SWISS
            else -> null
        }

        if (tournamentType != null)
            preferenceService.setRankPriority(tournamentType, RankPriorityConfig(priorityList[0], priorityList[1], priorityList[2]))
    }

    fun handleScoreConfigChange(win: Int, loss: Int, tie: Int) {

        val tournamentType = when (tournamentType.value) {

            roundRobinRadioButtonId -> TournamentType.ROUND_ROBIN
            swissRadioButtonId -> TournamentType.SWISS
            else -> null
        }

        if (tournamentType != null)
            preferenceService.setRankScore(tournamentType, RankScoreConfig(win * 0.5f, loss * 0.5f, tie * 0.5f))
    }
}