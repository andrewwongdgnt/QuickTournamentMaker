package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.data.tournament.IRankConfig
import com.dgnt.quickTournamentMaker.data.tournament.ParticipantType
import com.dgnt.quickTournamentMaker.data.tournament.Rank
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup
import com.dgnt.quickTournamentMaker.service.interfaces.IEliminationRankingHelperService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingService

class SurvivalRankingService : IRankingService {
    override fun calculate(roundGroups: List<RoundGroup>, rankConfig: IRankConfig): Rank {
        val rounds = roundGroups.first().rounds

        val known = rounds.map { it.matchUps.flatMap { listOf(it.participant1,it.participant2).filter { it.participantType==ParticipantType.NORMAL } }.toSet()}
        return Rank(known,setOf())
    }
}