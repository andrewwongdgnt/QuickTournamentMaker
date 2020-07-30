package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.data.tournament.MatchUpStatus
import com.dgnt.quickTournamentMaker.data.tournament.RoundGroup

interface IRoundUpdateService {
    /**
     * Update the match ups for the entire tournament. This probably means we cascade the participant to next round(s)
     *
     * @param roundGroups the entire tournament basically
     * @param roundGroupIndex index of round group
     * @param roundIndex index of round in a round group
     * @param matchUpIndex index of match up in a round
     */
    fun update(roundGroups: List<RoundGroup>, roundGroupIndex:Int, roundIndex:Int, matchUpIndex:Int)
}