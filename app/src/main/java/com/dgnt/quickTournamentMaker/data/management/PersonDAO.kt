package com.dgnt.quickTournamentMaker.data.management

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dgnt.quickTournamentMaker.data.base.BaseDAO

@Dao
interface PersonDAO : BaseDAO<PersonEntity> {

    @Query("SELECT * FROM $PERSON_TABLE")
    override fun getAll(): LiveData<List<PersonEntity>>

    @Query("UPDATE $PERSON_TABLE SET groupName = :groupName WHERE groupName IN (:oldGroupNames)")
    suspend fun updateGroup(oldGroupNames: List<String>, groupName: String): Int

    @Query("DELETE FROM $PERSON_TABLE WHERE groupName IN (:groupNames)")
    suspend fun deleteViaGroup(groupNames: List<String>)
}