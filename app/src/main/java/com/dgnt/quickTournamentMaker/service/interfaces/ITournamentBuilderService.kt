package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.Tournament
import com.dgnt.quickTournamentMaker.model.tournament.TournamentInformation

interface ITournamentBuilderService
{

    fun build(tournamentInfo:TournamentInformation):Tournament
}