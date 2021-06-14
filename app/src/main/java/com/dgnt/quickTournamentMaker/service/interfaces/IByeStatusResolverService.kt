package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.RoundGroup

interface IByeStatusResolverService {

    /**
     * Resolve all the status in the specified round group and round
     *
     * @param roundGroups the entire tournament basically
     * @param roundGroupRoundPair the list of round group and round pairs. First is roundGroupIndex, second is roundIndex
     */
    fun resolve(roundGroups: List<RoundGroup>, vararg roundGroupRoundPair: Pair<Int, Int>)
}