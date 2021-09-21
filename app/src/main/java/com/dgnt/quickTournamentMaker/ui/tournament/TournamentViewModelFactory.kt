package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentBuilderService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInitiatorService

class TournamentViewModelFactory(
    private val tournamentBuilderService: ITournamentBuilderService,
    private val tournamentInitiatorService: ITournamentInitiatorService,
    private val participantService: IParticipantService,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TournamentViewModel::class.java)) {
            return TournamentViewModel(
                tournamentBuilderService,
                tournamentInitiatorService,
                participantService
            ) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
