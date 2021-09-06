package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService

class EvenNumberSeedService : ISeedService {
    override fun seed(participants: List<Participant>): List<Participant> = (0 until if (participants.size % 2 == 0) participants.size else participants.size + 1).map { if (it < participants.size) participants[it] else Participant.BYE_PARTICIPANT }


}