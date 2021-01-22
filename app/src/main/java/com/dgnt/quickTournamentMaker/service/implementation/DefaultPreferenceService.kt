package com.dgnt.quickTournamentMaker.service.implementation

import android.content.SharedPreferences
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService

class DefaultPreferenceService(private val sharedPreferences: SharedPreferences, private val rankingConfigService: IRankingConfigService) : IPreferenceService {

    companion object {
        private const val PREF_SWISS_RANKING_CONFIG_KEY = "rankSwissRankingConfigKey"
        private const val PREF_ROUND_ROBIN_RANKING_CONFIG_KEY = "rankRoundRobinRankingConfigKey"

        private const val PREF_SWISS_RANK_PRIORITY_KEY = "rankSwissPriorityKey"
        private const val PREF_ROUND_ROBIN_RANK_PRIORITY_KEY = "rankRoundRobinPriorityKey"

        private const val PREF_SWISS_RANK_SCORE_KEY = "rankSwissScoreKey"
        private const val PREF_ROUND_ROBIN_RANK_SCORE_KEY = "rankRoundRobinScoreKey"
    }

    override fun isRankingBasedOnPriority(tournamentType: TournamentType): Boolean =
        when (tournamentType) {
            TournamentType.SWISS -> sharedPreferences.getBoolean(PREF_SWISS_RANKING_CONFIG_KEY, true)
            TournamentType.ROUND_ROBIN -> sharedPreferences.getBoolean(PREF_ROUND_ROBIN_RANKING_CONFIG_KEY, true)
            else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
        }


    override fun setRankingBasedOnPriority(tournamentType: TournamentType, value: Boolean) {
        val editor = sharedPreferences.edit()
        when (tournamentType) {
            TournamentType.SWISS -> editor.putBoolean(PREF_SWISS_RANKING_CONFIG_KEY, value)
            TournamentType.ROUND_ROBIN -> editor.putBoolean(PREF_ROUND_ROBIN_RANKING_CONFIG_KEY, value)
            else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
        }
        editor.commit()
    }

    override fun getRankPriority(tournamentType: TournamentType): RankPriorityConfig =
        rankingConfigService.buildRankingFromPriority(
            when (tournamentType) {
                TournamentType.SWISS -> sharedPreferences.getString(PREF_SWISS_RANK_PRIORITY_KEY, RankPriorityConfig.DEFAULT_INPUT)
                TournamentType.ROUND_ROBIN -> sharedPreferences.getString(PREF_ROUND_ROBIN_RANK_PRIORITY_KEY, RankPriorityConfig.DEFAULT_INPUT)
                else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
            }
        )


    override fun setRankPriority(tournamentType: TournamentType, rankPriorityConfig: RankPriorityConfig) {
        val editor = sharedPreferences.edit()
        editor.putString(
            when (tournamentType) {
                TournamentType.SWISS -> PREF_SWISS_RANK_PRIORITY_KEY
                TournamentType.ROUND_ROBIN -> PREF_ROUND_ROBIN_RANK_PRIORITY_KEY
                else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
            }, rankingConfigService.getRankingFromPriorityAsString(rankPriorityConfig)
        )
        editor.commit()

    }

    override fun getRankScore(tournamentType: TournamentType): RankScoreConfig =
        rankingConfigService.buildRankingFromScore(
            when (tournamentType) {
                TournamentType.SWISS -> sharedPreferences.getString(PREF_SWISS_RANK_SCORE_KEY, RankScoreConfig.DEFAULT_INPUT)
                TournamentType.ROUND_ROBIN -> sharedPreferences.getString(PREF_ROUND_ROBIN_RANK_SCORE_KEY, RankScoreConfig.DEFAULT_INPUT)
                else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
            }
        )


    override fun setRankScore(tournamentType: TournamentType, rankScoreConfig: RankScoreConfig) {
        val editor = sharedPreferences.edit()
        editor.putString(
            when (tournamentType) {
                TournamentType.SWISS -> PREF_SWISS_RANK_SCORE_KEY
                TournamentType.ROUND_ROBIN -> PREF_ROUND_ROBIN_RANK_SCORE_KEY
                else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
            }, rankingConfigService.getRankingFromScoreAsString(rankScoreConfig)
        )
        editor.commit()
    }
}