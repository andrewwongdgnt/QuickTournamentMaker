package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

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
    val earliestCreatedDateBacking = MutableLiveData<LocalDateTime>()

    @Bindable
    val latestCreatedDateFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val latestCreatedDate = MutableLiveData<String>()

    @Bindable
    val latestCreatedDateBacking = MutableLiveData<LocalDateTime>()

    @Bindable
    val earliestModifiedDateFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val earliestModifiedDate = MutableLiveData<String>()

    @Bindable
    val earliestModifiedDateBacking = MutableLiveData<LocalDateTime>()

    @Bindable
    val latestModifiedDateFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val latestModifiedDate = MutableLiveData<String>()

    @Bindable
    val latestModifiedDateBacking = MutableLiveData<LocalDateTime>()

    @Bindable
    val progressFilteredOn = MutableLiveData<Boolean>()

    @Bindable
    val progressRange = MutableLiveData<List<Float>>().apply { value = listOf(0f, 100f) }


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

        minParticipantsFilteredOn.value = preferenceService.isFilteredOnMinimumParticipants()
        minParticipants.value = preferenceService.getMinimumParticipantsToFilterOn().toString()

        maxParticipantsFilteredOn.value = preferenceService.isFilteredOnMaximumParticipants()
        maxParticipants.value = preferenceService.getMaximumParticipantsToFilterOn().toString()

        val dateFormat = DateTimeFormat.mediumDate()

        earliestCreatedDateFilteredOn.value = preferenceService.isFilteredOnEarliestCreatedDate()
        preferenceService.getEarliestCreatedDateToFilterOn().let {
            earliestCreatedDate.value = dateFormat.print(it)
            earliestCreatedDateBacking.value = it
        }

        latestCreatedDateFilteredOn.value = preferenceService.isFilteredOnLatestCreatedDate()
        preferenceService.getLatestCreatedDateToFilterOn().let {
            latestCreatedDate.value = dateFormat.print(it)
            latestCreatedDateBacking.value = it
        }

        earliestModifiedDateFilteredOn.value = preferenceService.isFilteredOnEarliestModifiedDate()
        preferenceService.getEarliestModifiedDateToFilterOn().let {
            earliestModifiedDate.value = dateFormat.print(it)
            earliestModifiedDateBacking.value = it
        }

        latestModifiedDateFilteredOn.value = preferenceService.isFilteredOnLatestModifiedDate()
        preferenceService.getLatestModifiedDateToFilterOn().let {
            latestModifiedDate.value = dateFormat.print(it)
            latestModifiedDateBacking.value = it
        }

        progressFilteredOn.value = preferenceService.isFilteredOnLeastProgress()
    }
}