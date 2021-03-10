package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService

class ByePopulateSeedService  : ISeedService  {
    override fun seed(participants: List<Participant>): List<Participant> = (0 until participants.size*2).map{if (it%2==0) participants[it/2] else Participant.BYE_PARTICIPANT}


}