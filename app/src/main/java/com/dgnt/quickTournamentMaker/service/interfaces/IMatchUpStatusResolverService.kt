package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup

interface IMatchUpStatusResolverService {

    /**
     * Resolve all the status in the specified round group and round
     *
     * @param roundGroups the entire tournament basically
     * @param roundGroupRoundPair the list of round group and round pairs
     */
    fun resolve(roundGroups: List<RoundGroup>, vararg roundGroupRoundPair: Pair<Int, Int>)
}