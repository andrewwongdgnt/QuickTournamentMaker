package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.Round
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IParticipantService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundGeneratorService
import com.dgnt.quickTournamentMaker.util.TournamentUtil

class EliminationRoundGeneratorService(private val participantService: IParticipantService) : IRoundGeneratorService {
    override fun build(orderedParticipants: List<Participant>): List<RoundGroup> {

        val round1 = participantService.createRound(orderedParticipants)

        val rounds: MutableList<Round> = ArrayList();
        rounds.add(round1)

        val totalParticipants = orderedParticipants.size

        var matchUpTotal: Double = totalParticipants / 4.0
        while (matchUpTotal >= 1) {

            val round = Round((1..matchUpTotal.toInt()).map { MatchUp(Participant.NULL_PARTICIPANT, Participant.NULL_PARTICIPANT) })
            rounds.add(round)

            matchUpTotal *= 0.5
        }

        return listOf(RoundGroup(rounds))
    }

}