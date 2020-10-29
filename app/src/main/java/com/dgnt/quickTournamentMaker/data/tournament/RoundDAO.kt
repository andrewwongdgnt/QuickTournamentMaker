package com.dgnt.quickTournamentMaker.data.tournament

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoundDAO {

    @Query("SELECT * FROM $ROUND_TABLE WHERE epoch = :epoch")
    fun getAll(epoch:Long):LiveData<List<RoundEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg entity: RoundEntity):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities:List<RoundEntity>):List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(vararg entity: RoundEntity):Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entities:List<RoundEntity>):Int

    @Delete
    suspend fun delete(vararg entity: RoundEntity)

    @Delete
    suspend fun delete(entities:List<RoundEntity>)
}