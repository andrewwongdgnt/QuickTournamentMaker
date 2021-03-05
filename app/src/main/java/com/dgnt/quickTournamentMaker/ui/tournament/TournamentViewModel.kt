package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation

class TournamentViewModel : ViewModel(), Observable {


    @Bindable
    val title = MutableLiveData<String>()

    @Bindable
    val description = MutableLiveData<String>()

    @Bindable
    val participants = MutableLiveData<List<Participant>>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(tournamentInformation: TournamentInformation) {
        title.value = tournamentInformation.title
        description.value = tournamentInformation.description
        participants.value = tournamentInformation.participants
    }
}