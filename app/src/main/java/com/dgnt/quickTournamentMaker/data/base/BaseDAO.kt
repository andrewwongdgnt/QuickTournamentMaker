package com.dgnt.quickTournamentMaker.data.base

import androidx.room.*


@Dao
interface BaseDAO<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entity: T): Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entities: List<T>): Int

    @Delete
    suspend fun delete(entity: T)

    @Delete
    suspend fun delete(entities: List<T>)
}