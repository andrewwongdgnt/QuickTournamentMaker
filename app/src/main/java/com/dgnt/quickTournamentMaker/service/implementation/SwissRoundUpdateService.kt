package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService
import com.dgnt.quickTournamentMaker.service.interfaces.IRoundUpdateService


class SwissRoundUpdateService(private val rankingService: IRankingService) : IRoundUpdateService {

    override fun update(roundGroups: List<RoundGroup>, roundGroupIndex: Int, roundIndex: Int, matchUpIndex: Int, rankConfig: IRankConfig) {

        val matchUpHistory = roundGroups.flatMap { it.rounds.flatMap { it.matchUps.map { toPair(it) } } }.toMutableSet()
        createNewRound(roundGroups, roundGroupIndex, roundIndex, matchUpHistory, rankConfig)

    }

    private fun createNewRound(roundGroups: List<RoundGroup>, roundGroupIndex: Int, roundIndex: Int, matchUpHistory: MutableSet<Pair<Participant, Participant>>, rankConfig: IRankConfig): Boolean {
        val rounds = roundGroups[roundGroupIndex].rounds
        if (roundIndex >= rounds.lastIndex) {
            return false
        }

        // Purge
        rounds.subList(roundIndex + 1, rounds.size).forEach { round ->
            round.matchUps.forEach { matchUp ->

                when (matchUp.status) {
                    MatchUpStatus.P1_WINNER -> {
                        matchUp.participant1.record.wins--;
                        matchUp.participant2.record.losses--;
                    }
                    MatchUpStatus.P2_WINNER -> {
                        matchUp.participant1.record.losses--;
                        matchUp.participant2.record.wins--;
                    }
                    MatchUpStatus.TIE -> {
                        matchUp.participant1.record.ties--;
                        matchUp.participant2.record.ties--;
                    }
                }

                matchUp.participant1 = Participant.NULL_PARTICIPANT
                matchUp.participant2 = Participant.NULL_PARTICIPANT

            }
        }

        val areAllMatchUpsResolved = rounds[roundIndex].matchUps.all { it.status != MatchUpStatus.DEFAULT && it.participant1.participantType != ParticipantType.NULL && it.participant2.participantType != ParticipantType.NULL }

        var newRoundBlocked = false
        if (areAllMatchUpsResolved) {
            val rankedParticipantList = rankingService.calculate(roundGroups, rankConfig).known.flatMap { it.sortedDescending() }.reversed()
            val rankedParticipantListFinal = if (rankedParticipantList.size % 2 == 1) rankedParticipantList + listOf(Participant.BYE_PARTICIPANT) else rankedParticipantList
            val pairList: List<Pair<Participant, Participant>>? = getProperPairing(rankedParticipantListFinal, matchUpHistory)
            if (pairList == null) {
                return true;
            } else {
                for (nextMatchUpIndex in pairList.indices) {
                    val (participant1, participant2) = pairList[nextMatchUpIndex]
                    matchUpHistory.add(toPair(participant1, participant2))
                    val nextMatchUp: MatchUp = roundGroups[roundGroupIndex].rounds[roundIndex + 1].matchUps[nextMatchUpIndex]
                    nextMatchUp.participant1 = participant1
                    nextMatchUp.participant2 = participant2

                    when (nextMatchUp.status) {
                        MatchUpStatus.P1_WINNER -> {
                            participant1.record.wins++
                            participant2.record.losses++
                        }
                        MatchUpStatus.P2_WINNER -> {
                            participant2.record.wins++
                            participant1.record.losses++
                        }
                        MatchUpStatus.TIE -> {
                            participant1.record.ties++
                            participant2.record.ties++
                        }
                    }
                }
            }
            newRoundBlocked = createNewRound(roundGroups, roundGroupIndex, roundIndex + 1, matchUpHistory, rankConfig) || newRoundBlocked;

        }
        return newRoundBlocked;
    }

    private fun getProperPairing(rankedParticipantList: List<Participant>, matchUpHistory: Set<Pair<Participant, Participant>>): List<Pair<Participant, Participant>>? {
        if (rankedParticipantList.size === 2) {
            val participant1 = rankedParticipantList[0]
            val participant2 = rankedParticipantList[1]
            return if (!matchUpHistory.contains(toPair(participant1, participant2))) {
                listOf(Pair(participant1, participant2))
            } else {
                null
            }
        } else if (rankedParticipantList.size > 2) {
            val participant1 = rankedParticipantList[0]
            for (i in 1 until rankedParticipantList.size) {
                val participant2 = rankedParticipantList[i]
                if (!matchUpHistory.contains(toPair(participant1, participant2))) {
                    val subRankedParticipantList: MutableList<Participant> = ArrayList()
                    for (j in 1 until rankedParticipantList.size) {
                        if (j != i) subRankedParticipantList.add(rankedParticipantList[j])
                    }
                    val subProperPairing = getProperPairing(subRankedParticipantList, matchUpHistory)
                    if (subProperPairing != null) {
                        return listOf(Pair(participant1, participant2)) + subProperPairing
                    }
                }
            }
            return null
        }
        return null
    }

    private fun toPair(matchUp: MatchUp): Pair<Participant, Participant> {
        return toPair(matchUp.participant1, matchUp.participant2)
    }


    private fun toPair(participant1: Participant, participant2: Participant): Pair<Participant, Participant> {
        return if (participant1.key <= participant2.key) Pair(participant1, participant2) else Pair(participant2, participant1)
    }
}