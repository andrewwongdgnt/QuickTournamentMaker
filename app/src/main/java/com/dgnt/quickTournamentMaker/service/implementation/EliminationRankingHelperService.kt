package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IEliminationRankingHelperService

class EliminationRankingHelperService : IEliminationRankingHelperService {
    override fun calculate(roundsFirst: List<Round>, roundsLoserBracketNonFinalist: List<Round>, roundsWinnerBracket: List<Round>): Rank {

        val known1 = roundsLoserBracketNonFinalist.map { round ->

            round.matchUps.map { matchUp ->
                when (matchUp.status) {
                    MatchUpStatus.P1_WINNER -> matchUp.participant2
                    MatchUpStatus.P2_WINNER -> matchUp.participant1
                    else -> Participant.NULL_PARTICIPANT
                }
            }.filter { it.participantType == ParticipantType.NORMAL }
                .toSet()


        }
        //Not sure if this works for triple eliminations but we'll cross that bridge when it comes

        //just assume there is one match up and its the same throughout
        val lastMatchUpWinnerBracket = roundsWinnerBracket.last().matchUps.first()
        var known2 = when {
            roundsWinnerBracket.any { it.matchUps.first().status == MatchUpStatus.P1_WINNER } -> {
                listOf(setOf(lastMatchUpWinnerBracket.participant2), setOf(lastMatchUpWinnerBracket.participant1))
            }
            lastMatchUpWinnerBracket.status ==  MatchUpStatus.P2_WINNER -> {
                listOf(setOf(lastMatchUpWinnerBracket.participant1), setOf(lastMatchUpWinnerBracket.participant2))
            }
            else -> {
                listOf(setOf<Participant>(), setOf())
            }
        }.map{ x ->
            x.filter { it.participantType == ParticipantType.NORMAL }.toSet()
        }

        val known = known1 + known2

        val unknown = roundsFirst.flatMap { round ->
            round.matchUps.flatMap { matchUp ->
                when (matchUp.status) {
                    MatchUpStatus.P1_WINNER, MatchUpStatus.P2_WINNER -> listOf(Participant.NULL_PARTICIPANT)
                    else -> listOf(matchUp.participant1, matchUp.participant2)
                }
            }.filter { it.participantType == ParticipantType.NORMAL }
        }.toSet()

        return Rank(known, unknown)
    }
}