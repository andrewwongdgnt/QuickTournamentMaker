package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TournamentDAO {

    @Query("SELECT * FROM $TOURNAMENT_TABLE WHERE epoch = :epoch")
    fun getAll(epoch:Long):LiveData<List<TournamentEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg entity: TournamentEntity):List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities:List<TournamentEntity>):List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(vararg entity: TournamentEntity):Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entities:List<TournamentEntity>):Int

    @Delete
    suspend fun delete(vararg entity: TournamentEntity)

    @Delete
    suspend fun delete(entities:List<TournamentEntity>)
}