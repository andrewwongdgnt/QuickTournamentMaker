package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MatchUpDAO {

    @Query("SELECT * FROM $MATCH_UP_TABLE WHERE epoch = :epoch")
    fun getAll(epoch:Long):LiveData<List<MatchUpEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg entity: MatchUpEntity):List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities:List<MatchUpEntity>):List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(vararg entity: MatchUpEntity):Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entities:List<MatchUpEntity>):Int

    @Delete
    suspend fun delete(vararg entity: MatchUpEntity)

    @Delete
    suspend fun delete(entities:List<MatchUpEntity>)
}