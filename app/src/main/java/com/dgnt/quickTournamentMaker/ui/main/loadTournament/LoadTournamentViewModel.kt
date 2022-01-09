package com.dgnt.quickTournamentMaker.ui.main.loadTournament

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import com.dgnt.quickTournamentMaker.data.tournament.IMatchUpRepository
import com.dgnt.quickTournamentMaker.data.tournament.IParticipantRepository
import com.dgnt.quickTournamentMaker.data.tournament.IRoundRepository
import com.dgnt.quickTournamentMaker.data.tournament.ITournamentRepository

class LoadTournamentViewModel(
    tournamentRepository: ITournamentRepository,
    roundRepository: IRoundRepository,
    matchUpRepository: IMatchUpRepository,
    participantRepository: IParticipantRepository,
) : ViewModel(), Observable {

    private val tournaments = tournamentRepository.getAll()
    private val rounds = roundRepository.getAll()
    private val matchUps = matchUpRepository.getAll()
    private val participants = participantRepository.getAll()


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }


}