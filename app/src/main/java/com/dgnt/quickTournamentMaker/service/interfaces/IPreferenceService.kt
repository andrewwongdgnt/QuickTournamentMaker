package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.loadTournament.Sort
import com.dgnt.quickTournamentMaker.model.loadTournament.ViewMode
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType

interface IPreferenceService {

    /**
     * determine if ranking is based on priority or not.
     * This is part of a legacy preference value where we just assume ranking only has priority or score.
     *
     * @param tournamentType the tournament type
     * @return whether this tournament type is based on priority ranking
     */
    fun isRankingBasedOnPriority(tournamentType:TournamentType):Boolean

    /**
     * for a given tournament type, set whether ranking is based on priority
     * This is part of a legacy preference value where we just assume ranking only has priority or score.
     *
     * @param tournamentType the tournament type
     * @param value whether this ranking is based on priority or not
     */
    fun setRankingBasedOnPriority(tournamentType:TournamentType, value:Boolean)

    /**
     * get the rank config for priority based ranking
     *
     * @param tournamentType the tournament type
     * @return the priority based ranking value
     */
    fun getRankPriority(tournamentType:TournamentType): RankPriorityConfig

    /**
     * set the rank config for priority based ranking
     *
     * @param tournamentType the tournament type
     * @param rankPriorityConfig the priority based ranking value
     */
    fun setRankPriority(tournamentType:TournamentType, rankPriorityConfig:RankPriorityConfig)

    /**
     * get the rank config for score based ranking
     *
     * @param tournamentType the tournament type
     * @return the score  based ranking value
     */
    fun getRankScore(tournamentType:TournamentType): RankScoreConfig

    /**
     * set the rank config for score based ranking
     *
     * @param tournamentType the tournament type
     * @param rankScoreConfig the score based ranking value
     */
    fun setRankScore(tournamentType: TournamentType, rankScoreConfig:RankScoreConfig)

    /**
     * get the sort value
     *
     * @return the sort value
     */
    fun getSort(): Sort

    /**
     * set the sort value
     *
     * @param sort the sort value
     */
    fun setSort(sort: Sort)

    /**
     * get the view mode
     *
     * @return the view mode
     */
    fun getViewMode(): ViewMode

    /**
     * set the view mode
     *
     * @param viewMode the view mode
     */
    fun setViewMode(viewMode: ViewMode)
}