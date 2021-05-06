package com.dgnt.quickTournamentMaker.ui.customSeed

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.service.interfaces.ICustomSeedManagementService

class CustomSeedViewModel(private val customSeedManagementService: ICustomSeedManagementService) : Observable, ViewModel() {

    @Bindable
    val matchUps = MutableLiveData<List<MatchUp>>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(orderedParticipants: List<Participant>) {
        matchUps.value = customSeedManagementService.setUp(orderedParticipants)
    }

    fun select(matchUpIndex: Int, isParticipant1Selected: Boolean) =
        customSeedManagementService.select(matchUpIndex, isParticipant1Selected)


}