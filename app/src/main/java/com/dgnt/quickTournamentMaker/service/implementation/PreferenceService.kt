package com.dgnt.quickTournamentMaker.service.implementation

import android.content.SharedPreferences
import com.dgnt.quickTournamentMaker.model.loadTournament.Sort
import com.dgnt.quickTournamentMaker.model.loadTournament.ViewMode
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

    override fun isRankingBasedOnPriority(tournamentType: TournamentType) =
        sharedPreferences.getBoolean(
            when (tournamentType) {
                TournamentType.SWISS -> PREF_SWISS_RANKING_CONFIG_KEY
                TournamentType.ROUND_ROBIN -> PREF_ROUND_ROBIN_RANKING_CONFIG_KEY
                else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
            }, true
        )


    override fun setRankingBasedOnPriority(tournamentType: TournamentType, value: Boolean) =
        sharedPreferences.edit().run {
            putBoolean(
                when (tournamentType) {
                    TournamentType.SWISS -> PREF_SWISS_RANKING_CONFIG_KEY
                    TournamentType.ROUND_ROBIN -> PREF_ROUND_ROBIN_RANKING_CONFIG_KEY
                    else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
                }, value
            )
            apply()
        }

    override fun getRankPriority(tournamentType: TournamentType) =
        rankingConfigService.buildRankingFromPriority(
            sharedPreferences.getString(
                when (tournamentType) {
                    TournamentType.SWISS -> PREF_SWISS_RANK_PRIORITY_KEY
                    TournamentType.ROUND_ROBIN -> PREF_ROUND_ROBIN_RANK_PRIORITY_KEY
                    else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
                }, RankPriorityConfig.DEFAULT_INPUT
            ) ?: RankPriorityConfig.DEFAULT_INPUT
        )


    override fun setRankPriority(tournamentType: TournamentType, rankPriorityConfig: RankPriorityConfig) =
        sharedPreferences.edit().run {
            putString(
                when (tournamentType) {
                    TournamentType.SWISS -> PREF_SWISS_RANK_PRIORITY_KEY
                    TournamentType.ROUND_ROBIN -> PREF_ROUND_ROBIN_RANK_PRIORITY_KEY
                    else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
                }, rankingConfigService.toString(rankPriorityConfig)
            )
            apply()
        }

    override fun getRankScore(tournamentType: TournamentType) =
        rankingConfigService.buildRankingFromScore(
            sharedPreferences.getString(
                when (tournamentType) {
                    TournamentType.SWISS -> PREF_SWISS_RANK_SCORE_KEY
                    TournamentType.ROUND_ROBIN -> PREF_ROUND_ROBIN_RANK_SCORE_KEY
                    else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
                }, RankScoreConfig.DEFAULT_INPUT
            ) ?: RankScoreConfig.DEFAULT_INPUT
        )


    override fun setRankScore(tournamentType: TournamentType, rankScoreConfig: RankScoreConfig) =
        sharedPreferences.edit().run {
            putString(
                when (tournamentType) {
                    TournamentType.SWISS -> PREF_SWISS_RANK_SCORE_KEY
                    TournamentType.ROUND_ROBIN -> PREF_ROUND_ROBIN_RANK_SCORE_KEY
                    else -> throw IllegalArgumentException("Tournament Type not supported $tournamentType")
                }, rankingConfigService.toString(rankScoreConfig)
            )
            apply()
        }


    override fun getSort() =
        try {
            Sort.valueOf(
                sharedPreferences.getString(
                    PREF_LOAD_TOURNAMENT_SORT_KEY,
                    Sort.CREATION_DATE_NEWEST.name
                ) ?: Sort.CREATION_DATE_NEWEST.name
            )
        } catch (e: java.lang.IllegalArgumentException) {
            Sort.CREATION_DATE_NEWEST
        }


    override fun setSort(sort: Sort) =
        sharedPreferences.edit().run {
            putString(PREF_LOAD_TOURNAMENT_SORT_KEY, sort.name)
            apply()
        }


    override fun getViewMode() =
        try {
            ViewMode.valueOf(
                sharedPreferences.getString(
                    PREF_LOAD_TOURNAMENT_VIEW_KEY,
                    ViewMode.BASIC.name
                ) ?: ViewMode.BASIC.name
            )
        } catch (e: java.lang.IllegalArgumentException) {
            ViewMode.BASIC
        }


    override fun setViewMode(viewMode: ViewMode) =
        sharedPreferences.edit().run {
            putString(PREF_LOAD_TOURNAMENT_VIEW_KEY, viewMode.name)
            apply()
        }


}