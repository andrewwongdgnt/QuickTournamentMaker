package com.dgnt.quickTournamentMaker.service.implementation

import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfigType
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService

class DefaultRankingConfigService : IRankingConfigService {

    private val winLossTieRegex = """w|l|t""".toRegex()


    override fun buildRankingFromPriority(value: String): RankPriorityConfig {
        val listValue = (value.split(";").takeIf {
            it.all { e -> e.matches(winLossTieRegex) } && it.distinct().size == it.size && it.size == 3
        } ?: RankPriorityConfig.DEFAULT_INPUT.split(";"))
            .map { RankPriorityConfigType.fromString(it) }
        return RankPriorityConfig(listValue[0], listValue[1], listValue[2])

    }

    override fun getRankingFromPriorityAsString(rankPriorityConfig: RankPriorityConfig): String = "${rankPriorityConfig.first.shorthand};${rankPriorityConfig.second.shorthand};${rankPriorityConfig.third.shorthand}"

    override fun buildRankingFromScore(value: String): RankScoreConfig {
        val listValue = (value.split(";").takeIf {
            it.all { e -> e.toFloatOrNull() != null } && it.size == 3
        } ?: RankScoreConfig.DEFAULT_INPUT.split(";"))
            .map { it.toFloat() }
        return RankScoreConfig(listValue[0], listValue[1], listValue[2])
    }

    override fun getRankingFromScoreAsString(rankScoreConfig: RankScoreConfig): String = "${rankScoreConfig.win};${rankScoreConfig.loss};${rankScoreConfig.tie}"

}