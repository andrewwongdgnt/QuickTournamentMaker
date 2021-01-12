package com.dgnt.quickTournamentMaker.ui.main.common

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService

interface TournamentTypeEditorViewModel {

    val preferenceService: IPreferenceService
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


    fun handleTournamentTypeChange(radioButtonId: Int, roundRobinRadioButtonId: Int, swissRadioButtonId: Int, compareRankFromPriorityRadioButtonId: Int, compareRankFromScoreRadioButton: Int) {
        val isRankingBasedOnPriority = when (radioButtonId) {
            roundRobinRadioButtonId -> preferenceService.isRankingBasedOnPriority(TournamentType.ROUND_ROBIN)
            swissRadioButtonId -> preferenceService.isRankingBasedOnPriority(TournamentType.SWISS)
            else -> null
        }

        if (isRankingBasedOnPriority != null)
            rankConfig.value = if (isRankingBasedOnPriority) compareRankFromPriorityRadioButtonId else compareRankFromScoreRadioButton

    }

    fun handleRankConfigHelpMsgChange(radioButtonId: Int, roundRobinRadioButtonId: Int, swissRadioButtonId: Int, rankConfigurationForRoundRobinHelpMsg: String, rankConfigurationForSwissHelpMsg: String) {
        if (radioButtonId == roundRobinRadioButtonId) {
            rankConfigHelpMsg.value = rankConfigurationForRoundRobinHelpMsg
        } else if (radioButtonId == swissRadioButtonId) {
            rankConfigHelpMsg.value = rankConfigurationForSwissHelpMsg
        }
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