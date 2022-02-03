package com.dgnt.quickTournamentMaker.service.implementation

import android.content.SharedPreferences
import com.dgnt.quickTournamentMaker.model.loadTournament.Sort
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import com.dgnt.quickTournamentMaker.service.interfaces.IPreferenceService
import com.dgnt.quickTournamentMaker.service.interfaces.IRankingConfigService

class PreferenceService(private val sharedPreferences: SharedPreferences, private val rankingConfigService: IRankingConfigService) : IPreferenceService {

    companion object {
        private const val PREF_SWISS_RANKING_CONFIG_KEY = "rankSwissRankingConfigKey"
        private const val PREF_ROUND_ROBIN_RANKING_CONFIG_KEY = "rankRoundRobinRankingConfigKey"

        private const val PREF_SWISS_RANK_PRIORITY_KEY = "rankSwissPriorityKey"
        private const val PREF_ROUND_ROBIN_RANK_PRIORITY_KEY = "rankRoundRobinPriorityKey"

        private const val PREF_SWISS_RANK_SCORE_KEY = "rankSwissScoreKey"
        private const val PREF_ROUND_ROBIN_RANK_SCORE_KEY = "rankRoundRobinScoreKey"

        private const val PREF_LOAD_TOURNAMENT_SORT_KEY = "historicalTournamentSortKey"

        private const val PREF_LOAD_TOURNAMENT_VIEW_KEY = "historicalTournamentViewKey"

        private const val PREF_FILTER_TOURNAMENT_TYPE_KEY = "filterTournamentTypeKey"

        private const val PREF_FILTER_MIN_PARTICIPANT_KEY = "filterMinimumParticipantKey"
        private const val PREF_FILTER_MIN_PARTICIPANT_ALLOWED_KEY = "filterMinimumParticipantAllowedKey"
        private const val PREF_FILTER_MAX_PARTICIPANT_KEY = "filterMaximumParticipantKey"
        private const val PREF_FILTER_MAX_PARTICIPANT_ALLOWED_KEY = "filterMaximumParticipantAllowedKey"

        private const val PREF_FILTER_EARLIEST_CREATED_EPOCH_KEY = "filterEarliestCreatedEpochKey"
        private const val PREF_FILTER_EARLIEST_CREATED_EPOCH_ALLOWED_KEY = "filterEarliestCreatedEpochAllowedKey"
        private const val PREF_FILTER_LATEST_CREATED_EPOCH_KEY = "filterLatestCreatedEpochKey"
        private const val PREF_FILTER_LATEST_CREATED_EPOCH_ALLOWED_KEY = "filterLatestCreatedEpochAllowedKey"

        private const val PREF_FILTER_EARLIEST_MODIFIED_EPOCH_KEY = "filterEarliestModifiedEpochKey"
        private const val PREF_FILTER_EARLIEST_MODIFIED_EPOCH_ALLOWED_KEY = "filterEarliestModifiedEpochAllowedKey"
        private const val PREF_FILTER_LATEST_MODIFIED_EPOCH_KEY = "filterLatestModifiedEpochKey"
        private const val PREF_FILTER_LATEST_MODIFIED_EPOCH_ALLOWED_KEY = "filterLatestModifiedEpochAllowedKey"

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
        editor.apply()
    }

    override fun getRankPriority(tournamentType: TournamentType): RankPriorityConfig =
        rankingConfigService.buildRankingFromPriority(
            when (tournamentType) {
                TournamentType.SWISS -> sharedPreferences.getString(PREF_SWISS_RANK_PRIORITY_KEY, RankPriorityConfig.DEFAULT_INPUT) ?: RankPriorityConfig.DEFAULT_INPUT
                TournamentType.ROUND_ROBIN -> sharedPreferences.getString(PREF_ROUND_ROBIN_RANK_PRIORITY_KEY, RankPriorityConfig.DEFAULT_INPUT) ?: RankPriorityConfig.DEFAULT_INPUT
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
            }, rankingConfigService.toString(rankPriorityConfig)
        )
        editor.apply()

    }

    override fun getRankScore(tournamentType: TournamentType): RankScoreConfig =
        rankingConfigService.buildRankingFromScore(
            when (tournamentType) {
                TournamentType.SWISS -> sharedPreferences.getString(PREF_SWISS_RANK_SCORE_KEY, RankScoreConfig.DEFAULT_INPUT) ?: RankScoreConfig.DEFAULT_INPUT
                TournamentType.ROUND_ROBIN -> sharedPreferences.getString(PREF_ROUND_ROBIN_RANK_SCORE_KEY, RankScoreConfig.DEFAULT_INPUT) ?: RankScoreConfig.DEFAULT_INPUT
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
            }, rankingConfigService.toString(rankScoreConfig)
        )
        editor.apply()
    }

    override fun getSort() =
        (sharedPreferences.getString(PREF_LOAD_TOURNAMENT_SORT_KEY, Sort.CREATION_DATE_NEWEST.name) ?: Sort.CREATION_DATE_NEWEST.name).let {
            try {
                Sort.valueOf(it)
            } catch (e: java.lang.IllegalArgumentException) {
                Sort.CREATION_DATE_NEWEST
            }
        }

    override fun setSort(sort: Sort) {
        val editor = sharedPreferences.edit()
        editor.putString(PREF_LOAD_TOURNAMENT_SORT_KEY, sort.name)
        editor.apply()
    }

}