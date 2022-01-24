package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.data.tournament.IMatchUpRepository
import com.dgnt.quickTournamentMaker.data.tournament.IParticipantRepository
import com.dgnt.quickTournamentMaker.data.tournament.IRoundRepository
import com.dgnt.quickTournamentMaker.data.tournament.ITournamentRepository
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentBuilderService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentDataTransformerService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentInitiatorService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentRestoreService

class TournamentViewModelFactory(
    private val tournamentBuilderService: ITournamentBuilderService,
    private val tournamentInitiatorService: ITournamentInitiatorService,
    private val tournamentRestoreService: ITournamentRestoreService,
    private val tournamentDataTransformerService: ITournamentDataTransformerService,
    private val tournamentRepository: ITournamentRepository,
    private val roundRepository: IRoundRepository,
    private val matchUpRepository: IMatchUpRepository,
    private val participantRepository: IParticipantRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TournamentViewModel::class.java)) {
            return TournamentViewModel(
                tournamentBuilderService,
                tournamentInitiatorService,
                tournamentRestoreService,
                tournamentDataTransformerService,
                tournamentRepository,
                roundRepository,
                matchUpRepository,
                participantRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
