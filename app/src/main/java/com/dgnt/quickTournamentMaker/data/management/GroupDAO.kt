package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroupDAO {

    @Query("SELECT * FROM $GROUP_TABLE")
    fun getAllGroups():LiveData<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroup(groupEntity:GroupEntity):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGroups(groupEntities:List<GroupEntity>):List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateGroup(groupEntity:GroupEntity):Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateGroups(groupEntities:List<GroupEntity>):Int

    @Delete
    suspend fun deleteGroup(groupEntity:GroupEntity)
}