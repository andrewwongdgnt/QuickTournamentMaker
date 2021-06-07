package com.dgnt.quickTournamentMaker.ui.tournament

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.*
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

    fun setData(tournamentInformation: TournamentInformation, orderedParticipants: List<Participant>, defaultRoundGroupTitleFunc: (RoundGroup) -> String, defaultRoundTitleFunc: (Round) -> String, defaultMatchUpTitleFunc: (MatchUp) -> String) {
        title.value = tournamentInformation.title
        tournament.value = tournamentBuilderService.build(tournamentInformation, orderedParticipants, defaultRoundGroupTitleFunc, defaultRoundTitleFunc, defaultMatchUpTitleFunc)
    }

    fun select(matchUp:MatchUp,  participantPosition:ParticipantPosition) {
        tournament.value?.let{
            matchUp.status = it.matchUpStatusTransformService.transform(matchUp.status, participantPosition)
            it.roundUpdateService.update(it.roundGroups, matchUp.roundGroupIndex, matchUp.roundIndex, matchUp.matchUpIndex,it.tournamentInformation.rankConfig)
        }
    }
}