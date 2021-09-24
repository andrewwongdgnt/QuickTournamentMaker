package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.*

interface  IRankingService {
    /**
     * calculate the rank of each participant. Both in known rankings and unknown
     *
     * @param roundGroups the entire tournament basically     *
     * @param rankConfig the rank config
     * @return the rank
     */
    fun calculate(roundGroups: List<RoundGroup>, rankConfig:IRankConfig = RankPriorityConfig(RankPriorityConfigType.WIN,RankPriorityConfigType.LOSS,RankPriorityConfigType.TIE)): Rank
}