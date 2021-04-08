package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentBuilderService

class TournamentViewModel(private val tournamentBuilderService: ITournamentBuilderService) : ViewModel(), Observable {


    @Bindable
    val title = MutableLiveData<String>()
    @Bindable
    val description = MutableLiveData<String>()

    @Bindable
    val tournament = MutableLiveData<Tournament>()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    fun setData(tournamentInformation: TournamentInformation, defaultRoundTitleFunc:(Round)-> String, defaultMatchUpTitleFunc: (MatchUp) -> String) {
        title.value = tournamentInformation.title
        tournament.value = tournamentBuilderService.build(tournamentInformation, defaultRoundTitleFunc, defaultMatchUpTitleFunc)
    }
}