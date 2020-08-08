package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.*
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService

/**
 * Used for round robin and swiss
 *
 */
class RecordRankingService : IRankingService {
    override fun calculate(roundGroups: List<RoundGroup>, rankConfig: IRankConfig): Rank {
        val participants = roundGroups.flatMap { rg -> rg.rounds.flatMap { r -> r.matchUps.flatMap { m -> setOf(m.participant1, m.participant2) } } }.toSet()
        val comparator = Comparator<Participant> { p1, p2 ->
            val p1Record = p1.record
            val p2Record = p2.record
            val stringTripleRepresentation = rankConfig.stringTripleRepresentation
            when (stringTripleRepresentation.first) {
                RankPriorityConfigType.WIN.shorthand, RankPriorityConfigType.LOSS.shorthand, RankPriorityConfigType.TIE.shorthand -> {
                    var ret = 0
                    for (s in listOf(stringTripleRepresentation.first, stringTripleRepresentation.second, stringTripleRepresentation.third)) {
                        ret = when (s) {
                            RankPriorityConfigType.WIN.shorthand -> p1Record.wins - p2Record.wins
                            RankPriorityConfigType.LOSS.shorthand -> p2Record.losses - p1Record.losses
                            else -> p1Record.ties - p2Record.ties
                        }
                        if (ret!=0)
                            break
                    }
                    ret
                }
                else -> {
                    val winScore = stringTripleRepresentation.first.toInt()
                    val lossScore = stringTripleRepresentation.first.toInt()
                    val tieScore = stringTripleRepresentation.first.toInt()

                    val p1Score = p1Record.wins * winScore + p1Record.ties * tieScore + p1Record.losses * lossScore
                    val p2Score = p2Record.wins * winScore + p2Record.ties * tieScore + p2Record.losses * lossScore

                    p1Score - p2Score;
                }

            }
        }
        val sorted = participants.sortedWith(comparator)
        val known = sorted.fold(mutableListOf<MutableList<Participant>>()) { acc, i ->
            if (acc.isEmpty() || comparator.compare(acc.last().last(),i)!=0) {
                acc.add(mutableListOf(i))
            } else acc.last().add(i)
            acc
        }.map{it.toSet()}

        return Rank(known,setOf())
    }
}