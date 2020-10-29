package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroupDAO {

    @Query("SELECT * FROM $GROUP_TABLE")
    fun getAll():LiveData<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg entity:GroupEntity):List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities:List<GroupEntity>):List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(vararg entity:GroupEntity):Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(entities:List<GroupEntity>):Int

    @Delete
    suspend fun delete(vararg entity:GroupEntity)

    @Delete
    suspend fun delete(entities:List<GroupEntity>)
}