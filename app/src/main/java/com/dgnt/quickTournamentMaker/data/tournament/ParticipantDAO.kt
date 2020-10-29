package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ParticipantDAO {

    @Query("SELECT * FROM $PARTICIPANT_TABLE WHERE epoch = :epoch")
    fun getAll(epoch:Long):LiveData<List<ParticipantEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg entity: ParticipantEntity):List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities:List<ParticipantEntity>):List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(vararg entity: ParticipantEntity):Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entities:List<ParticipantEntity>):Int

    @Delete
    suspend fun delete(vararg entity: ParticipantEntity)

    @Delete
    suspend fun delete(entities:List<ParticipantEntity>)
}