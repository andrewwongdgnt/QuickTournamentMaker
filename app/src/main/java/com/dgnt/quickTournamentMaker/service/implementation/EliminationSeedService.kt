package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.MatchUp
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.Round
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.ISeedService
import com.dgnt.quickTournamentMaker.util.TournamentUtil

class EliminationSeedService : ISeedService {
    override fun build(orderedParticipants: List<Participant>): List<RoundGroup> {

        if (!seedCheck(orderedParticipants)) return listOf()

        val round1 = TournamentUtil.createRound1(orderedParticipants)

        val rounds: MutableList<Round> = ArrayList<Round>();
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

    override fun seedCheck(orderedParticipants: List<Participant>): Boolean = TournamentUtil.basicSeedCheck(orderedParticipants) && TournamentUtil.isPowerOf2(orderedParticipants.size);

}