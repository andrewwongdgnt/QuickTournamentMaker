package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import org.joda.time.LocalDateTime

class LoadTournamentFilterOptionsViewModel(private val preferenceService: IPreferenceService) : ViewModel(), Observable, IPreferenceService by preferenceService {

    @Bindable
    val eliminationFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val doubleEliminationFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val roundRobinFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val swissFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val survivalFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val minParticipantsFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val minParticipants = MutableLiveData<String>()

    @Bindable
    val maxParticipantsFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val maxParticipants = MutableLiveData<String>()

    @Bindable
    val earliestCreatedDateFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val earliestCreatedDate = MutableLiveData<String>()

    @Bindable
    val latestCreatedDateFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val latestCreatedDate = MutableLiveData<String>()

    @Bindable
    val earliestModifiedDateFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val earliestModifiedDate = MutableLiveData<String>()

    @Bindable
    val latestModifiedDateFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val latestModifiedDate = MutableLiveData<String>()


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun init() {
        eliminationFilteredOn.value = preferenceService.isFilteredOnTournamentType(TournamentType.ELIMINATION)
        doubleEliminationFilteredOn.value = preferenceService.isFilteredOnTournamentType(TournamentType.DOUBLE_ELIMINATION)
        roundRobinFilteredOn.value = preferenceService.isFilteredOnTournamentType(TournamentType.ROUND_ROBIN)
        swissFilteredOn.value = preferenceService.isFilteredOnTournamentType(TournamentType.SURVIVAL)
        survivalFilteredOn.value = preferenceService.isFilteredOnTournamentType(TournamentType.SWISS)
    }
}