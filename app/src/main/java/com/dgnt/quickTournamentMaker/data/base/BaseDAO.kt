package com.dgnt.quickTournamentMaker.data.base

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dgnt.quickTournamentMaker.data.management.GroupEntity


@Dao
interface BaseDAO<T> {

    fun getAll(): LiveData<List<T>>

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