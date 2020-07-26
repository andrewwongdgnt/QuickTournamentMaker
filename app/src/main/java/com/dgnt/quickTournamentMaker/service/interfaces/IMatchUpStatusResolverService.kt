package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.data.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.data.tournament.Participant
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup

interface IMatchUpStatusResolverService {

    fun resolve(roundGroups: List<RoundGroup>, vararg roundGroupRoundPair: Pair<Int, Int>)
}