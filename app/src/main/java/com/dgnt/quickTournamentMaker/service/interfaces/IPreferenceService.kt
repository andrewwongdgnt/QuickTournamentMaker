package com.dgnt.quickTournamentMaker.service.interfaces

import com.dgnt.quickTournamentMaker.model.loadTournament.Sort
import com.dgnt.quickTournamentMaker.model.loadTournament.ViewMode
import com.dgnt.quickTournamentMaker.model.tournament.RankPriorityConfig
import com.dgnt.quickTournamentMaker.model.tournament.RankScoreConfig
import com.dgnt.quickTournamentMaker.model.tournament.TournamentType
import org.joda.time.LocalDateTime

interface IPreferenceService {

    /**
     * determine if ranking is based on priority or not.
     * This is part of a legacy preference value where we just assume ranking only has priority or score.
     *
     * @param tournamentType the tournament type
     * @return whether this tournament type is based on priority ranking
     */
    fun isRankingBasedOnPriority(tournamentType: TournamentType): Boolean

    /**
     * for a given tournament type, set whether ranking is based on priority
     * This is part of a legacy preference value where we just assume ranking only has priority or score.
     *
     * @param tournamentType the tournament type
     * @param value whether this ranking is based on priority or not
     */
    fun setRankingBasedOnPriority(tournamentType: TournamentType, value: Boolean)

    /**
     * get the rank config for priority based ranking
     *
     * @param tournamentType the tournament type
     * @return the priority based ranking value
     */
    fun getRankPriority(tournamentType: TournamentType): RankPriorityConfig

    /**
     * set the rank config for priority based ranking
     *
     * @param tournamentType the tournament type
     * @param rankPriorityConfig the priority based ranking value
     */
    fun setRankPriority(tournamentType: TournamentType, rankPriorityConfig: RankPriorityConfig)

    /**
     * get the rank config for score based ranking
     *
     * @param tournamentType the tournament type
     * @return the score  based ranking value
     */
    fun getRankScore(tournamentType: TournamentType): RankScoreConfig

    /**
     * set the rank config for score based ranking
     *
     * @param tournamentType the tournament type
     * @param rankScoreConfig the score based ranking value
     */
    fun setRankScore(tournamentType: TournamentType, rankScoreConfig: RankScoreConfig)

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

    /**
     * gets if a tournament type is being filtered on
     *
     * @param type the tournament type
     * @return is filtered on
     */
    fun isFilteredOnTournamentType(type: TournamentType): Boolean

    /**
     * sets if a tournament type is to be filtered on
     *
     * @param type the tournament type
     * @param on if this tournament type is to be filtered on
     */
    fun setFilteredOnTournamentType(type: TournamentType, on: Boolean)

    /**
     * if there is a filter on the minimum number of participants, get the number
     *
     * @return the number
     */
    fun getMinimumParticipantsToFilterOn(): Int

    /**
     * if there is a filter on the minimum number of participants, set the number
     *
     * @param num the minimum number of participants
     */
    fun setMinimumParticipantsToFilterOn(num: Int)

    /**
     * gets if the minimum number of participants is being filtered on
     *
     * @return the boolean value
     */
    fun isFilteredOnMinimumParticipants(): Boolean

    /**
     * sets if the minimum number of participants is to be filtered on
     *
     * @param on if filter is to be applied
     */
    fun setFilteredOnMinimumParticipants(on: Boolean)

    /**
     * if there is a filter on the maximum number of participants, get the number
     *
     * @return the number
     */
    fun getMaximumParticipantsToFilterOn(): Int

    /**
     * if there is a filter on the maximum number of participants, set the number
     *
     * @param num the maximum number of participants
     */
    fun setMaximumParticipantsToFilterOn(num: Int)

    /**
     * gets if the maximum number of participants is being filtered on
     *
     * @return the boolean value
     */
    fun isFilteredOnMaximumParticipants(): Boolean

    /**
     * sets if the maximum number of participants is to be filtered on
     *
     * @param on if filter is to be applied
     */
    fun setFilteredOnMaximumParticipants(on: Boolean)

    /**
     * if there is a filter on the earliest created date, get the date
     *
     * @return the date in date time to allow greater granularity
     */
    fun getEarliestCreatedDateToFilterOn(): LocalDateTime

    /**
     * if there is a filter on the earliest created date, set the date
     *
     * @param date the date in date time to allow greater granularity
     */
    fun setEarliestCreatedDateToFilterOn(date: LocalDateTime)

    /**
     * gets if the earliest created date is being filtered on
     *
     * @return the boolean value
     */
    fun isFilteredOnEarliestCreatedDate(): Boolean

    /**
     * sets if the earliest created date is to be filtered on
     *
     * @param on the boolean value
     */
    fun setFilteredOnEarliestCreatedDate(on: Boolean)

    /**
     * if there is a filter on the latest created date, get the date
     *
     * @return the date in date time to allow greater granularity
     */
    fun getLatestCreatedDateToFilterOn(): LocalDateTime

    /**
     * if there is a filter on the latest created date, set the date
     *
     * @param date the date in date time to allow greater granularity
     */
    fun setLatestCreatedDateToFilterOn(date: LocalDateTime)

    /**
     * gets if the latest created date is being filtered on
     *
     * @return the boolean value
     */
    fun isFilteredOnLatestCreatedDate(): Boolean

    /**
     * sets if the latest created date is to be filtered on
     *
     * @param on the boolean value
     */
    fun setFilteredOnLatestCreatedDate(on: Boolean)

    /**
     * if there is a filter on the earliest modified date, get the date
     *
     * @return the date in date time to allow greater granularity
     */
    fun getEarliestModifiedDateToFilterOn(): LocalDateTime

    /**
     * if there is a filter on the earliest modified date, set the date
     *
     * @param date the date in date time to allow greater granularity
     */
    fun setEarliestModifiedDateToFilterOn(date: LocalDateTime)

    /**
     * gets if the earliest modified date is being filtered on
     *
     * @return the boolean value
     */
    fun isFilteredOnEarliestModifiedDate(): Boolean

    /**
     * sets if the earliest modified date is to be filtered on
     *
     * @param on the boolean value
     */
    fun setFilteredOnEarliestModifiedDate(on: Boolean)

    /**
     * if there is a filter on the latest modified date, get the date
     *
     * @return the date in date time to allow greater granularity
     */
    fun getLatestModifiedDateToFilterOn(): LocalDateTime

    /**
     * if there is a filter on the latest modified date, set the date
     *
     * @param date the date in date time to allow greater granularity
     */
    fun setLatestModifiedDateToFilterOn(date: LocalDateTime)

    /**
     * gets if the latest modified date is being filtered on
     *
     * @return the boolean value
     */
    fun isFilteredOnLatestModifiedDate(): Boolean

    /**
     * sets if the latest modified date is to be filtered on
     *
     * @param on the boolean value
     */
    fun setFilteredOnLatestModifiedDate(on: Boolean)

    /**
     * if there is a filter on least progress, get the number
     *
     * @return the progress from 0 - 100
     */
    fun getLeastProgressToFilterOn(): Float

    /**
     * if there is a filter on least progress, set the number
     *
     * @param progress the progress from 0 - 100
     */
    fun setLeastProgressToFilterOn(progress: Float)

    /**
     * gets if progress is being filtered on
     *
     * @return the boolean value
     */
    fun isFilteredOnProgress(): Boolean

    /**
     * sets if progress is to be filtered on
     *
     * @param on the boolean value
     */
    fun setFilteredOnProgress(on: Boolean)

    /**
     * if there is a filter on most progress, get the number
     *
     * @return the progress from 0 - 100
     */
    fun getMostProgressToFilterOn(): Float

    /**
     * if there is a filter on most progress, set the number
     *
     * @param progress the progress from 0 - 100
     */
    fun setMostProgressToFilterOn(progress: Float)

    
}