package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.IRankConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig

interface IRankingConfigService {

    /**
     * parse string value into a priority rank config
     *
     * @param value the string value
     * @return a priority rank config
     */
    fun buildRankingFromPriority(value: String): RankPriorityConfig

    /**
     * parse string value into a score rank config
     *
     * @param value the string value
     * @return a score rank config
     */
    fun buildRankingFromScore(value: String): RankScoreConfig

    /**
     * transform the rank config into its string representation
     *
     * @param rankConfig the rank config
     * @return the string representation
     */
    fun toString(rankConfig: IRankConfig): String

    /**
     * parse string value to a generic rank config
     *
     * @param value the string value
     * @return a generic rank config
     */
    fun buildRankingFromString(value: String): IRankConfig
}