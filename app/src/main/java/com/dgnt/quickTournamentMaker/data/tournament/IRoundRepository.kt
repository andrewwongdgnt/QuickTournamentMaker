package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import org.joda.time.LocalDateTime

interface IRoundRepository {

    fun getAll(epoch: LocalDateTime): LiveData<List<RoundEntity>>
    suspend fun insert(vararg entity: RoundEntity): List<Long>
    suspend fun insert(entities: List<RoundEntity>): List<Long>
    suspend fun update(vararg entity: RoundEntity): Int
    suspend fun update(entities: List<RoundEntity>): Int
    suspend fun delete(vararg entity: RoundEntity)
    suspend fun delete(entities: List<RoundEntity>)
}