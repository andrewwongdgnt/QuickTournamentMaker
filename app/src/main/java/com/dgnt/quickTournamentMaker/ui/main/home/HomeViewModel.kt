package com.dgnt.quickTournamentMaker.ui.main.home

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.management.PersonRepository
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentGeneralEditorViewModel
import com.dgnt.quickTournamentMaker.ui.main.common.TournamentTypeEditorViewModel

class HomeViewModel(personRepository: PersonRepository, private val preferenceService: IPreferenceService) : ViewModel(), Observable, TournamentGeneralEditorViewModel, TournamentTypeEditorViewModel {

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
    val numberOfPlayers = MutableLiveData<String>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun something() {
        val g = title.value
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

        if (tournamentType != null)
            preferenceService.setRankingBasedOnPriority(tournamentType, value)
    }


}