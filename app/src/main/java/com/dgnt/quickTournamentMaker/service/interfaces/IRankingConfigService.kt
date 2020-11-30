package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig

interface IRankingConfigService {

    fun buildRankingFromPriority(value: String): RankPriorityConfig

    fun getRankingFromPriorityAsString(rankPriorityConfig: RankPriorityConfig): String

    fun buildRankingFromScore(value: String): RankScoreConfig

    fun getRankingFromScoreAsString(rankScoreConfig: RankScoreConfig): String

}