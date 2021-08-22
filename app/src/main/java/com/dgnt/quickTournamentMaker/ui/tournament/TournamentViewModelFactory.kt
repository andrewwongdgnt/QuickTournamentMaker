package com.dgnt.quickTournamentMaker.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dgnt.quickTournamentMaker.service.interfaces.IByeStatusResolverService
import com.dgnt.quickTournamentMaker.service.interfaces.ITournamentBuilderService

class TournamentViewModelFactory(private val tournamentBuilderService: ITournamentBuilderService, private val byeStatusResolverService: IByeStatusResolverService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TournamentViewModel::class.java)) {
            return TournamentViewModel(tournamentBuilderService, byeStatusResolverService) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
