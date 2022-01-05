package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import org.joda.time.LocalDateTime

interface IMatchUpRepository {

    fun getAll(epoch: LocalDateTime): LiveData<List<MatchUpEntity>>
    suspend fun insert(vararg entity: MatchUpEntity): List<Long>
    suspend fun insert(entities: List<MatchUpEntity>): List<Long>
    suspend fun update(vararg entity: MatchUpEntity): Int
    suspend fun update(entities: List<MatchUpEntity>): Int
    suspend fun delete(vararg entity: MatchUpEntity)
    suspend fun delete(entities: List<MatchUpEntity>)
}