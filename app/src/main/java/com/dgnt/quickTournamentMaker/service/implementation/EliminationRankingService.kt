package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService

class EliminationRankingService : IRankingService {
    override fun calculate(roundGroups: List<RoundGroup>): Rank {
        val rounds = roundGroups.first().rounds

        val known = (0..rounds.size).map { i ->
            if (i < rounds.size) {
                rounds[i].matchUps.map { matchUp ->
                    when (matchUp.status) {
                        MatchUpStatus.P1_WINNER -> matchUp.participant2
                        MatchUpStatus.P2_WINNER -> matchUp.participant1
                        else -> Participant.NULL_PARTICIPANT
                    }
                }.filter { it.participantType == ParticipantType.NORMAL }
                    .toSet()
            } else {
                val matchUp = rounds.last().matchUps.first();
                listOf(
                    when (matchUp.status) {
                        MatchUpStatus.P1_WINNER -> matchUp.participant1
                        MatchUpStatus.P2_WINNER -> matchUp.participant2
                        else -> Participant.NULL_PARTICIPANT
                    }
                ).filter { it.participantType == ParticipantType.NORMAL }
                    .toSet()
            }
        }

        val unknown = rounds.flatMap { round ->
            round.matchUps.flatMap { matchUp ->
                when (matchUp.status) {
                    MatchUpStatus.P1_WINNER, MatchUpStatus.P2_WINNER -> listOf(Participant.NULL_PARTICIPANT)
                    else -> listOf(matchUp.participant1, matchUp.participant2)
                }
            }.filter { it.participantType == ParticipantType.NORMAL }
        }.toSet()

        return Rank(known,unknown)
    }
}