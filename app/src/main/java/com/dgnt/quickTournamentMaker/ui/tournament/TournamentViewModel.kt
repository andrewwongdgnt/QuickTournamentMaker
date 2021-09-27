package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentBuilderService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInitiatorService

class TournamentViewModel(
    private val tournamentBuilderService: ITournamentBuilderService,
    private val tournamentInitiatorService: ITournamentInitiatorService
) : ViewModel(), Observable {


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

    fun setData(tournamentInformation: TournamentInformation, orderedParticipants: List<Participant>, defaultRoundGroupTitleFunc: (Int) -> String, defaultRoundTitleFunc: (Int) -> String, defaultMatchUpTitleFunc: (Int, Participant, Participant) -> String) {
        title.value = tournamentInformation.title
        description.value = tournamentInformation.description

        tournamentBuilderService.build(
            tournamentInformation,
            orderedParticipants,
            defaultRoundGroupTitleFunc,
            defaultRoundTitleFunc,
            defaultMatchUpTitleFunc
        ).let {
            tournamentInitiatorService.initiate(it)
            this.tournament.value = it
        }

    }

    fun updateTournament(matchUp: MatchUp, participantPosition: ParticipantPosition) {
        tournament.value?.let {
            matchUp.status = it.matchUpStatusTransformService.transform(matchUp.status, participantPosition)
            it.updateRound(matchUp.roundGroupIndex, matchUp.roundIndex, matchUp.matchUpIndex)
        }
    }
}