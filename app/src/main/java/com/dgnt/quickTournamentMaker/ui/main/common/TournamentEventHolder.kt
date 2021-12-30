package com.dgnt.quickTournamentMaker.ui.main.common

import androidx.lifecycle.LiveData
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation
import com.dgnt.quickTournamentMaker.util.Event

interface TournamentEventHolder {

    val tournamentEvent: LiveData<Event<Pair<TournamentInformation, List<Participant>>>>

    val customSeedTournamentEvent: LiveData<Event<Pair<TournamentInformation, List<Participant>>>>

    val failedToStartTournamentMessage: LiveData<Event<Boolean>>
}