package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.MatchUp
import com.dgnt.quickTournamentMaker.model.tournament.Participant
import com.dgnt.quickTournamentMaker.model.tournament.Round
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService

class ParticipantService : IParticipantService {
    override fun createRound(participants: List<Participant>, roundGroupIndex: Int, roundIndex: Int, defaultRoundTitleFunc: (Round) -> String, defaultMatchUpTitleFunc: (MatchUp) -> String): Round = Round(roundGroupIndex, roundIndex, participants.zipWithNext().filterIndexed { index, _ -> index % 2 == 0 }.mapIndexed { i, it -> MatchUp(roundGroupIndex, roundIndex, i, it.first, it.second).apply { title = defaultMatchUpTitleFunc(this) } }).apply { title = defaultRoundTitleFunc(this) }
}