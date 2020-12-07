package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData

interface IMatchUpRepository {

    fun getAll(epoch: Long): LiveData<List<MatchUpEntity>>
    suspend fun insert(vararg entity: MatchUpEntity): List<Long>
    suspend fun insert(entities: List<MatchUpEntity>): List<Long>
    suspend fun update(vararg entity: MatchUpEntity): Int
    suspend fun update(entities: List<MatchUpEntity>): Int
    suspend fun delete(vararg entity: MatchUpEntity)
    suspend fun delete(entities: List<MatchUpEntity>)
}